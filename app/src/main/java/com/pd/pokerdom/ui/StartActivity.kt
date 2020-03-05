package com.pd.pokerdom.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.pd.pokerdom.R
import com.pd.pokerdom.model.AppVersion
import com.pd.pokerdom.service.FCMService
import com.pd.pokerdom.ui.update.IUpdateDialog
import com.pd.pokerdom.ui.update.UpdateDialog
import com.pd.pokerdom.util.*
import kotlinx.android.synthetic.main.activity_start.*
import org.koin.android.viewmodel.ext.android.viewModel


class StartActivity : AppCompatActivity(R.layout.activity_start), IUpdateDialog {

    companion object {
        private const val PERMISSION_REQUEST_STORAGE = 100
        private const val URL_APK = "https://android.g2slt.com/play/tr/assets/pd.apk"
    }

    private val viewModel: MainViewModel by viewModel()
    private lateinit var downloadController: DownloadController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkNotification()
    }

    private fun checkNotification() {
        intent.extras?.let {
            if (!it.isEmpty) {
                if (it.containsKey(FCMService.KEY_FCM_LINK)) {
                    Log.d("Firebase", "[StartActivity] KEY_LINK: ${it[FCMService.KEY_FCM_LINK]}")
                    MainActivity.open(context = this, link = it[FCMService.KEY_FCM_LINK].toString())
                }
            }
        } ?: checkAppVersion()
    }

    private fun checkAppVersion() {
        viewModel.getAppVersion()
        viewModel.appVersion.observe(this, Observer { appVersion ->
            val serverVersionLimit = appVersion.versionLimit.toString()
            val serverVersion = appVersion.version.toString()
            when {
                checkForUpdate(serverVersionLimit) -> showDialog(version = appVersion, lock = false)
                checkForUpdate(serverVersion) -> showDialog(version = appVersion, lock = true)
                else -> MainActivity.open(this)
            }
        })
    }


    private fun showDialog(version: AppVersion, lock: Boolean) {
        UpdateDialog.newInstance(this, version, lock)
            .show(supportFragmentManager, "dialog")
    }

    override fun doPositiveClick() {
        downloadController = DownloadController(this, URL_APK)
        checkStoragePermission()
    }

    override fun doNegativeClick() {
        MainActivity.open(this)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadController.enqueueDownload()
                } else {
                    mainLayout.showSnackbar(R.string.storage_permission_denied, Snackbar.LENGTH_SHORT)
                }
            }
        }
    }


    private fun checkStoragePermission() {
        if (checkSelfPermissionCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            downloadController.enqueueDownload()
        } else {
            requestStoragePermission()
        }
    }

    private fun requestStoragePermission() {
        if (shouldShowRequestPermissionRationaleCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            mainLayout.showSnackbar(R.string.storage_access_required, Snackbar.LENGTH_INDEFINITE, android.R.string.ok) {
                requestPermissionsCompat(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_STORAGE
                )
            }
        } else {
            requestPermissionsCompat(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_STORAGE)
        }
    }

}