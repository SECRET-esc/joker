package com.pd.pokerdom.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import com.pd.pokerdom.js.JavaScriptInterface
import com.pd.pokerdom.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Activity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val REQUEST_FILE_CHOOSER = 100
    }

    private var fileValueCallback: ValueCallback<Array<Uri>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView.settings.loadsImagesAutomatically = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.userAgentString = "Mozilla/5.0 (Linux; Android 7.0; Redmi Note 4 Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.111 Mobile Safari/537.36"
        webView.settings.useWideViewPort = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.allowFileAccess = true
        webView.settings.allowFileAccessFromFileURLs = true
        webView.settings.allowUniversalAccessFromFileURLs = true
        webView.settings.setAppCacheEnabled(false)
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        webView.webViewClient = MyWebViewClient()
        webView.webChromeClient = MyWebChromeClient()
        webView.isFocusable = true
        webView.isFocusableInTouchMode = true
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.isScrollbarFadingEnabled = false
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

//        webView.loadUrl("http://194.67.78.242/4YRDMJ");
        webView.loadUrl("https://android.g2slt.com")
//        webView.loadUrl("https://develop.pokerteam.online/");

        webView.addJavascriptInterface(JavaScriptInterface(/*this*/), "Android")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webView.restoreState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
            Log.d(TAG, "shouldInterceptRequest: ${request?.url}")
            return super.shouldInterceptRequest(view, request)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val host = Uri.parse(url).host
            Log.w(TAG, "shouldOverrideUrlLoading $url")
            if (host != null && host.contains("pokerdom")) { // This is my website, so do not override; let my WebView load the page
                return false
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            return true
        }
    }

    private inner class MyWebChromeClient : WebChromeClient() {
        //For Android 5.0+
        override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?,
                                       fileChooserParams: FileChooserParams?): Boolean {
            Log.i("onShowFileChooser", "openFileChooser()3 called.")
            if (fileValueCallback != null) {
                fileValueCallback!!.onReceiveValue(null)
            }
            fileValueCallback = filePathCallback

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"

            startActivityForResult(intent, REQUEST_FILE_CHOOSER)
            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        var results: Array<Uri>? = null
        //Check if response is positive
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FILE_CHOOSER) {
                if (fileValueCallback == null) {
                    return
                }
                if (intent != null) { //Capture Photo if no image available
                    val dataString = intent.dataString
                    if (dataString != null) {
                        results = arrayOf(Uri.parse(dataString))
                    }
                }
            }
        } //WebChromeClient.FileChooserParams.parseResult(resultCode, data)
        fileValueCallback = if (results != null) {
            fileValueCallback!!.onReceiveValue(results)
            null
        } else {
            fileValueCallback!!.onReceiveValue(arrayOf())
            null
        }

    }


}