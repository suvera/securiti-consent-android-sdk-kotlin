package ai.securiti.consent.prefcenter

import ai.securiti.android.prefcenter.R
import ai.securiti.consent.prefcenter.ui.singlecolumn.SingleColumnFragmentDialog
import ai.securiti.consent.prefcenter.ui.webview.WebViewFragment
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.*


@Suppress("PrivatePropertyName")
class PreferenceCenterSDK(private val conf: Configuration, val appCtx: Context) {

    companion object {
        const val UI_SINGLE_COLUMN = 1
        const val UI_WEB_VIEW = 2
    }

    private val client = OkHttpClient()
    private val TAG = "PreferenceCenterSDK"
    private val FragmentManagerId = "PreferenceCenterFragmentManager"
    private var prefCenter: PreferenceCenter? = null

    private fun newRequest(): Request.Builder {
        return Request.Builder()
            .addHeader("Content-Type", "application/json")
            .addHeader("x-auth-token", conf.prefCenterToken)
            .addHeader("X-CMP-PrefCenter-Id", conf.prefCenterId)
    }

    private fun fetchPrefCenter(
        callback: PreferenceCenterFetchCallback,
        lang: String = "",
        location: String = ""
    ): Int {
        if (prefCenter != null) {
            callback.onSuccess(prefCenter as PreferenceCenter)
            return 0
        }
        Toast.makeText(appCtx, appCtx.getString(R.string.securiti_fetching_pref_center), Toast.LENGTH_LONG).show()

        Log.e(TAG, conf.toString())

        val httpUrl: HttpUrl
        try {
            httpUrl = (conf.apiHost + "/privaci/v1/consent/geolocation_preference_center").toHttpUrl()
        } catch (e: Exception) {
            Log.e(TAG, e.stackTraceToString())
            throw e
        }

        var urlBuilder = httpUrl.newBuilder()

        if (lang.isNotEmpty()) {
            urlBuilder = urlBuilder.addQueryParameter("language_code", lang)
        } else if (conf.lang.isNotEmpty()) {
            urlBuilder = urlBuilder.addQueryParameter("language_code", conf.lang)
        }

        if (location.isNotEmpty()) {
            urlBuilder = urlBuilder.addQueryParameter("location_code", location)
            conf.location = location
        } else if (conf.location.isNotEmpty()) {
            urlBuilder = urlBuilder.addQueryParameter("location_code", conf.location)
        }

        val url = urlBuilder.build()

        val request = newRequest()
            .url(url)
            .build()

        try {
            client.newCall(request).enqueue(callback)
        } catch (e: Exception) {
            Log.e(TAG, e.stackTraceToString())
        }

        println("empty preference center loaded")
        return 1
    }

    /**
     * Show Preference Center
     */
    fun showPreferenceCenter(
        activity: Any? = null,
        uiStyle: Int = 0
    ): Int {
        if (activity !is FragmentManager) {
            val msg = "Could not understand Activity " + activity?.javaClass.toString()
            Log.e(TAG, msg)
            Toast.makeText(appCtx, msg, Toast.LENGTH_SHORT).show()
            return 0
        }

        if (activity is FragmentManager && uiStyle == UI_WEB_VIEW) {
            renderFragmentManagerWebView(activity)
            return 0
        }

        val self = this
        val callback = object : PreferenceCenterFetchCallback {
            override fun onSuccess(prefCenter: PreferenceCenter) {
                if (activity is FragmentManager) {
                    when (uiStyle) {
                        UI_SINGLE_COLUMN -> {
                            self.renderFragmentManager(activity, prefCenter!!, uiStyle)
                        }

                        else -> {
                            val msg = "Could not understand UI Style $uiStyle"
                            Log.e(TAG, msg)
                        }
                    }
                } else {
                    val msg = "Could not understand Activity " + activity.javaClass.toString()
                    Log.e(TAG, msg)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, e.stackTraceToString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected http response $response")
                    }

                    val body = response.body!!.string()
                    println(body)
                    val mapper = Utils.mapper

                    try {
                        val prefCenter: PreferenceCenter = mapper.readValue(body)
                        onSuccess(prefCenter)
                    } catch (e: Exception) {
                        Log.e(TAG, e.stackTraceToString())
                        // print this error ?
                    }
                }
            }
        }

        fetchPrefCenter(callback)

        return 0
    }

    private fun renderFragmentManagerWebView(activity: FragmentManager): Int {
        val webView = WebViewFragment(conf, appCtx, null)
        val parent = getVisibleFragment(activity)
        val parentId: Int = parent?.id ?: 0

        activity.beginTransaction().add(parentId, webView).commit()

        return 0
    }

    private fun renderFragmentManager(activity: FragmentManager, prefCenter: PreferenceCenter, uiStyle: Int): Int {
        SingleColumnFragmentDialog(this, prefCenter).show(activity, FragmentManagerId)
        return 0
    }

    private fun getVisibleFragment(fragmentManager: FragmentManager): Fragment? {
        val fragments: List<Fragment> = fragmentManager.fragments
        for (fragment in fragments) {
            if (fragment.isVisible)
                return fragment
        }
        return null
    }

    fun saveConsent(consents: List<Purpose>): Int {
        val payload = ConsentPayload()
        val consentInfo = HashMap<Int, ConsentPayloadConsentInfo>()

        for (consent in consents) {
            if (!consentInfo.containsKey(consent.processing.id)) {
                consentInfo[consent.processing.id] = ConsentPayloadConsentInfo()
            }

            val item = ConsentPayloadConsentItem()
            item.consentPurposeId = consent.consent.id
            item.granted = consent.granted

            consentInfo[consent.processing.id]?.processingPurposeId = consent.processing.id
            consentInfo[consent.processing.id]?.consentedItems?.add(item)
        }

        if (consentInfo.size == 0) {
            return 1
        }

        payload.timestamp = (System.currentTimeMillis() / 1000).toInt()
        payload.consentInfo = ArrayList(consentInfo.values)

        val userUuid = UUID.randomUUID().toString()
        Toast.makeText(appCtx, appCtx.getString(R.string.securiti_saving_consents), Toast.LENGTH_SHORT).show()

        Log.e(TAG, conf.toString())

        val httpUrl: HttpUrl
        try {
            httpUrl = (conf.apiHost + "/privaci/v1/consent/geolocation_prefcenter/singleupload").toHttpUrl()
        } catch (e: Exception) {
            Log.e(TAG, e.stackTraceToString())
            throw e
        }

        val urlBuilder = httpUrl.newBuilder().addQueryParameter("location_code", conf.location.ifEmpty { "IN" })
        val url = urlBuilder.build()

        val jsonStr = Utils.mapper.writeValueAsString(payload)
        println(jsonStr)

        val jsonBody: RequestBody = jsonStr.toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = newRequest()
            .addHeader("X-CMP-UUID", userUuid)
            .post(jsonBody)
            .url(url)
            .build()

        val callback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, e.stackTraceToString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        val body = response.body!!.string()
                        println(body)
                        Log.e(TAG, "Unexpected http response $response")
                        return
                    }
                    val body = response.body!!.string()
                    println(body)
                    Log.i(TAG, "Consents recorded successfully")
                }
            }
        }

        try {
            client.newCall(request).enqueue(callback)
        } catch (e: Exception) {
            Log.e(TAG, e.stackTraceToString())
            Toast.makeText(
                appCtx,
                appCtx.getString(R.string.securiti_saving_consents_failed),
                Toast.LENGTH_SHORT
            ).show()
        }

        return 0
    }
}

private interface PreferenceCenterFetchCallback : Callback {
    fun onSuccess(prefCenter: PreferenceCenter)
}



