package com.calendar.loyalfans.activities


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.calendar.loyalfans.R
import com.calendar.loyalfans.retrofit.APIServices
import com.calendar.loyalfans.utils.RequestParams
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.layout_toolbar_back.*
import kotlinx.android.synthetic.main.layout_toolbar_textview.tvToolBarName

open class WebViewActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setContentView(R.layout.activity_webview)
        val webViewURL = intent.getStringExtra(RequestParams.WEBVIEW_URL).toString()
        if (webViewURL.contains(APIServices.W9_FORM_WEB_URL)) {
            tvToolBarName.text = getString(R.string.w_9_form)
        } else {
            tvToolBarName.text = getString(R.string.image_viewer)
        }
        imgBack.setOnClickListener(this)
        setUpWebView(webViewURL)

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView(webViewURL: String) {
        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.loadsImagesAutomatically = true
        settings.javaScriptEnabled = true
        settings.builtInZoomControls = true
        settings.mediaPlaybackRequiresUserGesture = false;
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.loadUrl(webViewURL)
        if (webViewURL.contains(APIServices.W9_FORM_DOWNLOAD_URL)) {
            webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                if (url != null) {
                    if (webViewURL != url) {
                        finish()
                    } else {
                        view.loadUrl(url)
                    }
                }
                return false // then it is not handled by default action
            }
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if (v.id == R.id.imgBack) {
                onBackPressed()
            }
        }
    }
}


