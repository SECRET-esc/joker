package com.pokerdom.pokerdom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    private WebView mWebView;
    private static final String TAG = "MainActivity";

    private ValueCallback<Uri[]> mUMA;
    private final static int FCR = 1;
    private String mCM = null;
    DataReceiver receiver;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.webview);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setScrollbarFadingEnabled(false);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 7.0; Redmi Note 4 Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.111 Mobile Safari/537.36");
        settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAppCacheEnabled(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setPluginState(WebSettings.PluginState.ON_DEMAND);

        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
        } else {
            cookieManager.setAcceptCookie(true);
        }

        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);


        if (savedInstanceState != null)
            mWebView.restoreState(savedInstanceState);
        else {
//            mWebView.loadUrl("http://194.67.78.242/4YRDMJ");
            mWebView.loadUrl("https://android.g2slt.com");
//            mWebView.loadUrl("https://develop.pokerteam.online/");
        }
        receiver = new DataReceiver();
        registerReceiver(receiver, new IntentFilter("PUSH_REGISTERED"));  //<----Register


        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        FirebaseMessaging.getInstance().subscribeToTopic("all");

    }

    class DataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if ("PUSH_REGISTERED".equals(intent.getAction())) {
                String pushToken = intent.getStringExtra("PUSH");
                String javaScript = "javascript:pushToken='" + pushToken + "';";
                mWebView.loadUrl(javaScript);
                Log.d(TAG, "javascript:console.log('" + pushToken + "');alert('" + pushToken + "');");
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(receiver);           //<-- Unregister to avoid memoryleak
    }


    @Override
    protected void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mWebView.restoreState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        mWebView.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            Log.d(TAG, "shouldInterceptRequest: " + url);

            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String host = Uri.parse(url).getHost();
            Uri.parse(url);
            Log.w(TAG, "shouldOverrideUrlLoading " + url + Uri.parse(url).toString());
            if (host != null && host.contains("pokerdom")) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
//            if (host.contains("facebook")) {
//                return false;
//            }
//            return false;
//            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs

//
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        //For Android 5.0+
        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                WebChromeClient.FileChooserParams fileChooserParams) {
            Log.i("onShowFileChooser", "openFileChooser()3 called.");
            if (mUMA != null) {
                mUMA.onReceiveValue(null);
            }
            mUMA = filePathCallback;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCM);
                } catch (IOException ex) {
                    Log.e(TAG, "Image file creation failed", ex);
                }
                if (photoFile != null) {
                    mCM = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }
            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("*/*");
            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
            } else {
                intentArray = new Intent[0];
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
            startActivityForResult(chooserIntent, FCR);
            return true;
        }


    }

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/don_test/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCM = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        Uri[] results = null;
        //Check if response is positive
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FCR) {
                if (mUMA == null) {
                    return;
                }
                if (intent == null) {
                    //Capture Photo if no image available
                    if (mCM != null) {
                        results = new Uri[]{Uri.parse(mCM)};
                        mCM = null;
                    }
                } else {
                    String dataString = intent.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
        }//WebChromeClient.FileChooserParams.parseResult(resultCode, data)
        if (results != null) {
            mUMA.onReceiveValue(results);
            mUMA = null;
        } else {
            mUMA.onReceiveValue(new Uri[]{});
            mUMA = null;
        }

    }
}
