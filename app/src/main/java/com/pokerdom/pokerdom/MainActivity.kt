package com.pokerdom.pokerdom

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity() {

    private var mUMA: ValueCallback<Array<Uri>>? = null
    private var mCM: String? = null
//    private var receiver: DataReceiver = DataReceiver()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settings = webView.settings
        settings.javaScriptEnabled = true
        webView.webViewClient = MyWebViewClient()
        webView.webChromeClient = MyWebChromeClient()
        webView.isFocusable = true
        webView.isFocusableInTouchMode = true
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.isScrollbarFadingEnabled = false

        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.loadsImagesAutomatically = true
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.userAgentString = "Mozilla/5.0 (Linux; Android 7.0; Redmi Note 4 Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.111 Mobile Safari/537.36"
        settings.useWideViewPort = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.allowFileAccess = true
        settings.allowFileAccessFromFileURLs = true
        settings.allowUniversalAccessFromFileURLs = true
        settings.setAppCacheEnabled(false)
        settings.cacheMode = WebSettings.LOAD_NO_CACHE

        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptThirdPartyCookies(webView, true)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState)
        } else {
            //            webView.loadUrl("http://194.67.78.242/4YRDMJ");
            webView.loadUrl("https://android.g2slt.com")
            //            webView.loadUrl("https://develop.pokerteam.online/");
        }
//        receiver = DataReceiver()
//        registerReceiver(receiver, IntentFilter("PUSH_REGISTERED")) //<----Register
//        if (intent.extras != null) {
//            for (key in intent.extras!!.keySet()) {
//                val value = intent.extras!![key]
//                Log.d(TAG, "Key: $key Value: $value")
//            }
//        }
//        FirebaseMessaging.getInstance().subscribeToTopic("all")
    }

//    inner class DataReceiver : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            if ("PUSH_REGISTERED" == intent.action) {
//                val pushToken = intent.getStringExtra("PUSH")
//                val javaScript = "javascript:pushToken='$pushToken';"
//                webView.loadUrl(javaScript)
//                Log.d(TAG, "javascript:console.log('$pushToken');alert('$pushToken');")
//            }
//        }
//    }

//    public override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(receiver) //<-- Unregister to avoid memoryleak
//    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webView.restoreState(savedInstanceState)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
            Log.d(TAG, "shouldInterceptRequest: $url")
            return super.shouldInterceptRequest(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val host = Uri.parse(url).host
            Uri.parse(url)
            Log.w(TAG, "shouldOverrideUrlLoading " + url + Uri.parse(url).toString())
            if (host != null && host.contains("pokerdom")) { // This is my website, so do not override; let my WebView load the page
                return false
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            return true
            //            if (host.contains("facebook")) {
//                return false;
//            }
//            return false;
//            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
//
        }
    }

    private inner class MyWebChromeClient : WebChromeClient() {
        //For Android 5.0+
        override fun onShowFileChooser(
                webView: WebView, filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams): Boolean {
            Log.i("onShowFileChooser", "openFileChooser()3 called.")
            if (mUMA != null) {
                mUMA!!.onReceiveValue(null)
            }
            mUMA = filePathCallback
            var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent!!.resolveActivity(this@MainActivity.packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                    takePictureIntent.putExtra("PhotoPath", mCM)
                } catch (ex: IOException) {
                    Log.e(TAG, "Image file creation failed", ex)
                }
                if (photoFile != null) {
                    mCM = "file:" + photoFile.absolutePath
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                } else {
                    takePictureIntent = null
                }
            }
            val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
            contentSelectionIntent.type = "*/*"

            val intentArray: Array<Intent> = takePictureIntent?.let { arrayOf(it) } ?: arrayOf()

            val chooserIntent = Intent(Intent.ACTION_CHOOSER)
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
            startActivityForResult(chooserIntent, FCR)
            return true
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {

        @SuppressLint("SimpleDateFormat")
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = File(Environment.getExternalStorageDirectory().absolutePath + "/don_test/")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        val image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        mCM = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        var results: Array<Uri>? = null
        //Check if response is positive
        if (resultCode == RESULT_OK) {
            if (requestCode == FCR) {
                if (mUMA == null) {
                    return
                }
                if (intent == null) { //Capture Photo if no image available
                    if (mCM != null) {
                        results = arrayOf(Uri.parse(mCM))
                        mCM = null
                    }
                } else {
                    val dataString = intent.dataString
                    if (dataString != null) {
                        results = arrayOf(Uri.parse(dataString))
                    }
                }
            }
        } //WebChromeClient.FileChooserParams.parseResult(resultCode, data)
        mUMA = if (results != null) {
            mUMA!!.onReceiveValue(results)
            null
        } else {
            mUMA!!.onReceiveValue(arrayOf())
            null
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val FCR = 1
    }
}