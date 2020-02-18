package com.pd.pokerdom.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.pd.pokerdom.BuildConfig

fun AppCompatActivity.checkSelfPermissionCompat(permission: String) =
    ActivityCompat.checkSelfPermission(this, permission)

fun AppCompatActivity.shouldShowRequestPermissionRationaleCompat(permission: String) =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

fun AppCompatActivity.requestPermissionsCompat(permissionsArray: Array<String>, requestCode: Int) {
    ActivityCompat.requestPermissions(this, permissionsArray, requestCode)
}


fun View.showSnackbar(msgId: Int, length: Int) {
    showSnackbar(context.getString(msgId), length)
}

fun View.showSnackbar(msg: String, length: Int) {
    showSnackbar(msg, length, null, {})
}

fun View.showSnackbar(msgId: Int, length: Int, actionMessageId: Int, action: (View) -> Unit) {
    showSnackbar(context.getString(msgId), length, context.getString(actionMessageId), action)
}

fun View.showSnackbar(
    msg: String,
    length: Int,
    actionMessage: CharSequence?,
    action: (View) -> Unit
) {
    val snackbar = Snackbar.make(this, msg, length)
    if (actionMessage != null) {
        snackbar.setAction(actionMessage) {
            action(this)
        }.show()
    }
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun checkForUpdate(serverVersion: String): Boolean {
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
}