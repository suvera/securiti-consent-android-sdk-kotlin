package ai.securiti.consent.prefcenter.ui.webview

import ai.securiti.android.prefcenter.R
import ai.securiti.consent.prefcenter.Configuration
import ai.securiti.consent.prefcenter.ConsentActivityListener
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment

class WebViewFragment(
    private val conf: Configuration,
    private val appCtx: Context,
    private val activityListener: ConsentActivityListener? = null
) : Fragment() {

    fun getAppCtx(): Context {
        return appCtx
    }

    fun getConf(): Configuration {
        return conf
    }

    fun getActivityListener(): ConsentActivityListener? {
        return activityListener
    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.title = "Team B";

        val v = inflater.inflate(R.layout.pref_center_webview, container, false)

        val webView = v.findViewById<WebView>(R.id.id_pref_center_webview)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        if (activityListener != null) {
            webView.addJavascriptInterface(WebAppInterface(this), "Android")
        }

        // TODO
        webView.loadUrl("https://dsp-qa.securiti.xyz/#/form-purpose-preference/b2bc21e0-0db5-4947-8208-a05436ccea72/acfb3617-f4cc-415d-9382-7ec1ac2c136f")

        return v
    }

}