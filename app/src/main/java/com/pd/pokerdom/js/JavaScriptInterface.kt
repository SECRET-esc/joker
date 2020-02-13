package com.pd.pokerdom.js

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface

class JavaScriptInterface(/*var ctx: Context*/) {

    @JavascriptInterface
    fun setUserId(userId: String?, customUserId: String?) {
        Log.d("JavascriptInterface", "userId - $userId , customUserId - $customUserId")
    }

}