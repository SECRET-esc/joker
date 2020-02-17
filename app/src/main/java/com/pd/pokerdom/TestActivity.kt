package com.pd.pokerdom

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.pd.pokerdom.util.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.net.URL


class TestActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_REQUEST_STORAGE = 100
        const val PERMISSION_REQUEST_INSTALL = 101
        //        const val URL_APK = "https://fs25.fex.net/download/2684558370"
        const val URL_APK = "https://android.g2slt.com/play/tr/assets/pd.apk"
    }

    private lateinit var downloadController: DownloadController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        downloadController = DownloadController(this, URL_APK)
        checkStoragePermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            // Request for permission.
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // start downloading
//                downloadController.enqueueDownload()
                startDownloading()
//                checkInstallPermission()
            } else {
                // Permission request was denied.
                mainLayout.showSnackbar(R.string.storage_permission_denied, Snackbar.LENGTH_SHORT)
            }
        }
    }


    private fun checkStoragePermission() {
        // Check if the storage permission has been granted
        if (checkSelfPermissionCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // start downloading
//            downloadController.enqueueDownload()
            startDownloading()
        } else {
            // Permission is missing and must be requested.
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
            requestPermissionsCompat(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_STORAGE
            )
        }
    }

    private fun startDownloading() {
        val uri = Uri.parse(URL_APK)
        val fileName = uri.pathSegments.last()

        val request = DownloadManager.Request(uri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(fileName)
        request.setDescription("The file is downloading...")

        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        request.setMimeType("application/vnd.android.package-archive");

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }
}
