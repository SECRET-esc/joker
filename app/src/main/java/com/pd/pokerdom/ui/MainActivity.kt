package com.pd.pokerdom.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.pd.pokerdom.BuildConfig
import com.pd.pokerdom.R
import com.pd.pokerdom.model.AppVersion
import com.pd.pokerdom.ui.update.UpdateDialog
import com.pd.pokerdom.util.*
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        private const val PERMISSION_REQUEST_STORAGE = 100
        private const val PERMISSION_REQUEST_INSTALL = 101
//        const val URL_APK = "https://fs25.fex.net/download/2684558370"
        private const val URL_APK = "https://android.g2slt.com/play/tr/assets/pd.apk"
    }

    private val viewModel: MainViewModel by viewModel()
    private lateinit var downloadController: DownloadController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val versionName: String = BuildConfig.VERSION_NAME
        Log.d("MyTeg", "versionName - $versionName")

        viewModel.getAppVersion()

        viewModel.appVersion.observe(this, Observer { appVersion ->
            val thisVersionName: Int = BuildConfig.VERSION_NAME.replace(".", "").toInt()
            val serverVersionName: Int = appVersion.version.toString().replace(".", "").toInt()

//            val thisVersionName: Int = "1.9.100".replace(".", "").toInt()
//            val serverVersionName: Int = "1.10.1".replace(".", "").toInt()
            if (thisVersionName < serverVersionName) {
                showDialog(appVersion)
            }
        })
    }

    private fun showDialog(version: AppVersion) {
        val newFragment = UpdateDialog.newInstance(version)
        newFragment.show(supportFragmentManager, "dialog")
    }

    fun doPositiveClick() {
//        Toast.makeText(this, "update", Toast.LENGTH_LONG).show()
        downloadController = DownloadController(this, URL_APK)
        checkStoragePermission()

//        checkInstallPermission()
    }

//    private fun checkInstallPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if (!packageManager.canRequestPackageInstalls()) {
//                startActivityForResult(
//                        Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
//                                .setData(Uri.parse(String.format("package:%s", packageName))), PERMISSION_REQUEST_INSTALL)
//            } else {
//                toast("Permissions granted.")
//                downloadController.enqueueDownload()
//            }
//        } else {
//            toast("Permissions granted.")
//            downloadController.enqueueDownload()
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PERMISSION_REQUEST_INSTALL && resultCode == Activity.RESULT_OK) {
//            if (packageManager.canRequestPackageInstalls()) {
//                toast("Permissions granted.")
//                downloadController.enqueueDownload()
//            }
//        } else { //give the error
//            checkInstallPermission()
//        }
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            // Request for permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // start downloading
                downloadController.enqueueDownload()
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
            downloadController.enqueueDownload()
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
            requestPermissionsCompat(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_STORAGE)
        }
    }

}