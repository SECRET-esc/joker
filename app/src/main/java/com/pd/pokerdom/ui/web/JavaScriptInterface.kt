package com.pd.pokerdom.ui.web

import android.util.Log
import android.webkit.JavascriptInterface

class JavaScriptInterface(private val viewModel: WebViewModel) {

    @JavascriptInterface
    fun setUserId(userId: String?, customUserId: String?) {
        Log.d("JavascriptInterface", "userId - $userId , customUserId - $customUserId")
        viewModel.sentTokenToServer(userId = userId, customUserId = customUserId)
    }

}