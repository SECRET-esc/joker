package com.pd.pokerdom.storage

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var firstRun: Boolean
        get() = prefs.getValue(KEY_FIRST_RUN, true)
        set(value) = prefs.setValue(KEY_FIRST_RUN, value)

    var tokenFCM: String?
        get() = prefs.getValue(KEY_TOKEN_FIREBASE, "")
        set(value) = prefs.setValue(KEY_TOKEN_FIREBASE, value)



    companion object {
        const val PREFS_NAME = "AppPrefs"
        const val KEY_FIRST_RUN = "firstRun"
        const val KEY_TOKEN_FIREBASE = "tokenFCM"

    }
}