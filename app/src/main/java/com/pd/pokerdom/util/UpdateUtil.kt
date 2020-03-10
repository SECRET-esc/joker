package com.pd.pokerdom.util

import com.pd.pokerdom.BuildConfig

fun checkForUpdate(serverVersion: String): Boolean {
    try {
        val thisVersion = BuildConfig.VERSION_NAME

        if (thisVersion.isEmpty() || serverVersion.isEmpty()) return false

        val existingVersionMajor = thisVersion.substringBefore(".")
        val newVersionMajor = serverVersion.substringBefore(".")
        if (existingVersionMajor.toInt() < newVersionMajor.toInt()) return true

        val existingVersionMinor = thisVersion.substringAfter(".").substringBefore(".")
        val newVersionMinor = serverVersion.substringAfter(".").substringBefore(".")
        if (existingVersionMinor.toInt() < newVersionMinor.toInt()) return true

        val existingVersionPatch = thisVersion.substringAfterLast(".")
        val newVersionPatch = serverVersion.substringAfterLast(".")
        return existingVersionPatch.toInt() < newVersionPatch.toInt()
    } catch (e: Exception) {
        return false
    }
}