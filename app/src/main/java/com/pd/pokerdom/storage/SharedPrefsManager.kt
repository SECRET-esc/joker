package com.pd.pokerdom.storage

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsManager(context: Context) {

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
//        get() = prefs.getValue(KEY_CONFIG_DOMAIN, "https://android.g2slt.com") // prod
        get() = prefs.getValue(KEY_CONFIG_DOMAIN, "https://develop.pokerdom.dev/") // dev
        set(value) = prefs.setValue(KEY_CONFIG_DOMAIN, value)



    companion object {
        const val PREFS_NAME = "AppPrefs"
        const val KEY_FIRST_RUN = "firstRun"
        const val KEY_TOKEN_FIREBASE = "tokenFCM"
        const val KEY_USER_ID = "userId"
        const val KEY_CUSTOM_USER_ID = "customUserId"
        const val KEY_CONFIG_DOMAIN = "configDomain"

    }
}