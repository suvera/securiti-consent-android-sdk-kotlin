package ai.securiti.consent.prefcenter.ui.webview

import ai.securiti.consent.prefcenter.Consent
import ai.securiti.consent.prefcenter.Utils
import android.util.Log
import android.webkit.JavascriptInterface
import com.fasterxml.jackson.module.kotlin.readValue

class WebAppInterface(private val webViewFragment: WebViewFragment) {

    @JavascriptInterface
    fun onPurposeLoaded(consent: String) {
        val mapper = Utils.mapper
        try {
            val consents: List<Consent> = mapper.readValue(consent)

            if (webViewFragment.getActivityListener() != null) {
                webViewFragment.getActivityListener()?.onPurposeLoaded(consents)
            }

        } catch (e: Exception) {
            Log.e("WebAppInterface", e.stackTraceToString())
        }
    }

    @JavascriptInterface
    fun onConsentsSubmitted(consent: String) {
        val mapper = Utils.mapper
        try {
            val consents: List<Consent> = mapper.readValue(consent)
            if (webViewFragment.getActivityListener() != null) {
                webViewFragment.getActivityListener()?.onConsentsSubmitted(consents)
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