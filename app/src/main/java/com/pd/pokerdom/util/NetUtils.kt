package com.pd.pokerdom.util

import android.content.Context
import android.net.ConnectivityManager


@Suppress("DEPRECATION")
fun Context.isConnecting(): Boolean {
    val connectManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectManager.activeNetworkInfo
    return networkInfo?.isConnectedOrConnecting == true
}

fun Context.isNotConnecting(): Boolean {
    return !isConnecting()
}