package ai.securiti.consent.prefcenter.ui.webview

import ai.securiti.consent.prefcenter.Consent
import ai.securiti.consent.prefcenter.Utils
import android.util.Log
import android.webkit.JavascriptInterface
import com.fasterxml.jackson.module.kotlin.readValue

class WebAppInterface(private val webViewFragment: WebViewFragment) {

    @JavascriptInterface
    fun onPreferenceCenterLoaded(consent: String) {
        val mapper = Utils.mapper
        try {
            val consents: List<Consent> = mapper.readValue(consent)

            if (webViewFragment.getActivityListener() != null) {
                webViewFragment.getActivityListener()?.onPreferenceCenterLoaded(consents)
            }

        } catch (e: Exception) {
            Log.e("WebAppInterface", e.stackTraceToString())
        }
    }

    @JavascriptInterface
    fun onPreferenceCenterLoadFailed(err: String) {
        val mapper = Utils.mapper
        try {
            if (webViewFragment.getActivityListener() != null) {
                webViewFragment.getActivityListener()?.onPreferenceCenterLoadFailed(RuntimeException(err))
            }

        } catch (e: Exception) {
            Log.e("WebAppInterface", e.stackTraceToString())
        }
    }

    @JavascriptInterface
    fun onConsentsSaved(consent: String) {
        val mapper = Utils.mapper
        try {
            val consents: List<Consent> = mapper.readValue(consent)
            if (webViewFragment.getActivityListener() != null) {
                webViewFragment.getActivityListener()?.onConsentsSaved(consents)
            }
        } catch (e: Exception) {
            Log.e("WebAppInterface", e.stackTraceToString())
        }
    }

    @JavascriptInterface
    fun onConsentsSaveFailed(consent: String, err: String) {
        val mapper = Utils.mapper
        try {
            val consents: List<Consent> = mapper.readValue(consent)
            if (webViewFragment.getActivityListener() != null) {
                webViewFragment.getActivityListener()?.onConsentsSaveFailed(consents, RuntimeException(err))
            }
        } catch (e: Exception) {
            Log.e("WebAppInterface", e.stackTraceToString())
        }
    }

    @JavascriptInterface
    fun onConsentsCancelled() {
        if (webViewFragment.getActivityListener() != null) {
            webViewFragment.getActivityListener()?.onConsentsCancelled()
        }
    }
}