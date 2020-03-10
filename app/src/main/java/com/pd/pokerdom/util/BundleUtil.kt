package com.pd.pokerdom.util

import android.os.Bundle
import android.util.Log

fun printListBundle(bundle: Bundle?) {
    if (bundle != null) {
        Log.d("MyLog", "bundle.size() = " + bundle.size().toString())
        for (key in bundle.keySet()) {
            Log.d("MyLog", key + " : " + if (bundle.get(key) != null) bundle.get(key) else "NULL")
        }
    }
}