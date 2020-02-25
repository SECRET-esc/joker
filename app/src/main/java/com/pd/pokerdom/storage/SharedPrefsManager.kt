package com.pd.pokerdom.storage

import android.content.Context
import android.content.SharedPreferences
import com.pd.pokerdom.BuildConfig

class SharedPrefsManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "AppPrefs"
        private const val KEY_FIRST_RUN = "firstRun"
        private const val KEY_TOKEN_FIREBASE = "tokenFCM"
        private const val KEY_USER_ID = "userId"
        private const val KEY_CUSTOM_USER_ID = "customUserId"
        private const val KEY_CONFIG_DOMAIN = "configDomain"

        private const val linkProd = "https://android.g2slt.com" // prod
        private const val linkDev = "https://develop.pokerdom.dev" // dev
        fun getLink() = if (BuildConfig.DEBUG) linkDev else linkProd
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var firstRun: Boolean
        get() = prefs.getValue(KEY_FIRST_RUN, true)
        set(value) = prefs.setValue(KEY_FIRST_RUN, value)

    var tokenFCM: String
        get() = prefs.getValue(KEY_TOKEN_FIREBASE, "")
        set(value) = prefs.setValue(KEY_TOKEN_FIREBASE, value)

    var userId: String?
        get() = prefs.getValue(KEY_USER_ID, "")
        set(value) = prefs.setValue(KEY_USER_ID, value)

    var customUserId: String?
        get() = prefs.getValue(KEY_CUSTOM_USER_ID, "")
        set(value) = prefs.setValue(KEY_CUSTOM_USER_ID, value)

    var configDomain: String
//        get() = prefs.getValue(KEY_CONFIG_DOMAIN, getLink())
        get() = prefs.getValue(KEY_CONFIG_DOMAIN, linkProd)
        set(value) = prefs.setValue(KEY_CONFIG_DOMAIN, value)

}