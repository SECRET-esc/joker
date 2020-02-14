package com.pd.pokerdom.ui.web

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.pd.pokerdom.R
import kotlinx.android.synthetic.main.fragment_web.*
import org.koin.android.viewmodel.ext.android.viewModel

class WebFragment : Fragment(R.layout.fragment_web) {

    companion object {
        private const val TAG = "WebFragment"
        private const val REQUEST_FILE_CHOOSER = 100
    }
    private val viewModel: WebViewModel by viewModel()
    private var fileValueCallback: ValueCallback<Array<Uri>>? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupWebView()

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState)
        }else {
//        webView.loadUrl("http://194.67.78.242/4YRDMJ");
        webView.loadUrl("https://android.g2slt.com")
//        webView.loadUrl("https://develop.pokerteam.online/");
        }
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

        webView.setOnKeyListener { _, _, keyEvent ->
            if (keyEvent.keyCode == KeyEvent.KEYCODE_BACK && !webView.canGoBack()) {
                false
            } else if (keyEvent.keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == MotionEvent.ACTION_UP) {
                webView.goBack()
                true
            } else true
        }

        webView.addJavascriptInterface(
            JavaScriptInterface(
                viewModel
            ), "Android")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        webView.saveState(outState)
        super.onSaveInstanceState(outState)
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

            startActivityForResult(intent,
                REQUEST_FILE_CHOOSER
            )
            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        var results: Array<Uri>? = null
        //Check if response is positive
        if (resultCode == AppCompatActivity.RESULT_OK) {
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
