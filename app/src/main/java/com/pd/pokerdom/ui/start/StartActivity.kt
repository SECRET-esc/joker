package com.pd.pokerdom.ui.start

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.pd.pokerdom.BuildConfig
import com.pd.pokerdom.R
import com.pd.pokerdom.service.FCMService
import com.pd.pokerdom.service.FCMService.Companion.KEY_FCM_LINK
import com.pd.pokerdom.service.FCMService.Companion.KEY_FROM_NOTIFICATION
import com.pd.pokerdom.storage.SharedPrefsManager
import com.pd.pokerdom.ui.ApplicationState
import com.pd.pokerdom.ui.connection.ConnectionStateActivity
import com.pd.pokerdom.ui.connection.ConnectivityReceiver
import com.pd.pokerdom.ui.main.MainActivity
import com.pd.pokerdom.ui.update.IUpdateDialog
import com.pd.pokerdom.ui.update.UpdateDialog
import com.pd.pokerdom.ui.update.UpdateDialog.UPDATE_DIALOG
import com.pd.pokerdom.ui.version.VersionActivityFragment
import com.pd.pokerdom.ui.version.VersionControl
import com.pd.pokerdom.util.*
import kotlinx.android.synthetic.main.activity_start.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class StartActivity : AppCompatActivity(R.layout.activity_start), IUpdateDialog, ConnectivityReceiver.ConnectivityReceiverListener {

    companion object {
        private const val PERMISSION_REQUEST_STORAGE = 100
    }

    private var versionChecked: Boolean = false;
    private val prefs: SharedPrefsManager by inject()
    private val versionControl: VersionControl by viewModel()
//    private val viewModel: StartViewModel by viewModel()
    private lateinit var downloadController: DownloadController
    private val navController: NavController by lazy { Navigation.findNavController(this, R.id.nav_host_fragment) }
    private var originSite: String? = null;
    private val applicationState = ApplicationState().initActivity()
    private var fromNotification: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent.extras
        val originSite = intent?.getString(KEY_FCM_LINK)
        val isFromNotification = intent?.getBoolean(KEY_FROM_NOTIFICATION)
        if (isFromNotification != null && !isAppRunning()) {
            registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            Log.d("isApplicationRunning", "app started at the first time from notification ${intent?.getBoolean(KEY_FROM_NOTIFICATION)}")
            originSite.let {
                this.originSite = it
                this.fromNotification = true
            }
            openMain()
        } else if(isAppRunning() && isFromNotification != null) {
            Log.d("isApplicationRunning", "app started at the second time from notification ${intent?.getBoolean(KEY_FROM_NOTIFICATION)}")
            originSite.let {
                this.originSite = it
                this.fromNotification = true
            }
            openMain()
        } else if (isFromNotification == null) {
            Log.d("isApplicationRunning", "default ${intent?.getBoolean(KEY_FROM_NOTIFICATION)}")
            registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            checkAppVersion()
        }
        Log.d("Mylog", "[StartActivity]")

    }

    private fun isAppRunning() : Boolean {
        val services = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).runningAppProcesses
        return services.firstOrNull{it.processName.equals(packageName,true)} != null
    }

    override fun onDestroy() {
        super.onDestroy()
        prefs.isFirstLunch = true
    }

    override fun onResume() {
        super.onResume()
        Log.d("Observer", "Resume")
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        checkConnection(isConnected)
    }

    private fun checkConnection(isConnected: Boolean) {
        Log.d("Observer", "State: $isConnected")
        if (!isConnected) {
            Log.d("Observer", "isConnected: $isConnected")
            ConnectionStateActivity.open(this)
        } else {
//            val int = intent.extras
//            val fromNotification: Boolean = int?.getBoolean(KEY_FROM_NOTIFICATION) as Boolean
            if (!versionChecked) {
                versionChecked = true
//                checkHostException()
//                if (!fromNotification) {
//                applicationState.onNext(true)
//                applicationState.subscribe { state ->
//                    Log.d("Observer", "KEY_FROM_NOTIFICATION: $state")
//                    if (!state) {
                        checkAppVersion()
//                    } else {
//                        val intent = intent.extras
//                        val originSite = intent?.getString(KEY_FCM_LINK)
//                        originSite.let {
//                            this.originSite = it
//                            this.fromNotification = state
//                            openMain()
//                        }
//                        throw Error("originSite is null")
//                    }
//                }

            } else {
                openMain()
            }

//            if (networkType()) {
//                Toast.makeText(this, "You are online now.!!!" + "\n Connected to Wifi Network", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(this, "You are online now.!!!" + "\n Connected to Cellular Network", Toast.LENGTH_LONG).show()
//            }
        }
    }
//
//    private fun networkType(): Boolean {
//        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
//        val isWifi: Boolean = activeNetwork?.type == ConnectivityManager.TYPE_WIFI
//        return isWifi
//    }


//    private fun checkHostException() {
//        viewModel.hostException.observe(this, Observer {
//            toast(it)
//        })
//    }
    private fun checkNotification() {
        val bundle = intent.extras
        printListBundle(bundle)

        val link = bundle?.getString(FCMService.KEY_FCM_LINK)
        Log.d("MyLog", "[StartActivity] link: $link")
        link?.let {
            Log.d("Firebase", "[StartActivity] KEY_LINK: $it")
            MainActivity.open(context = this, link = it)
        } ?: checkAppVersion()
    }

    private fun checkAppVersion() {

        val response: Array<Any> = versionControl.getVersion()

        val errorLimit: Boolean = response[0] as Boolean
        originSite = response[1] as String
        if (errorLimit) {
            return VersionActivityFragment.open(this)
        } else {
            openMain()
        }
//        if (isNotConnecting()) {
//            ConnectionStateActivity.open(this)
//            return
//        }
//        viewModel.getAppVersion()
//        viewModel.appVersion.observe(this, { appVersion ->
//            val serverVersionLimit = appVersion.versionLimit.toString()
//            val serverVersion = appVersion.version.toString()
//            originSite = appVersion.site.toString()
//            Log.d("siteTestLink", "Link ${appVersion.site.toString()}")
//            compareVersion(serverVersionLimit)
            // checking on app version

//        }
//    )






//        viewModel.openMain.observe(this, Observer {
//            if (it) openMain()
//        })
    }


//    private fun compareVersion(versionLimit: String) {
//        if (Version(versionLimit).isHigherThan(BuildConfig.VERSION_NAME)) {
//            val bool = Version(versionLimit).isHigherThan(BuildConfig.VERSION_NAME)
//            Log.d("MyLog", "[VERSION_NAME] --- $bool")
//            return VersionActivityFragment.open(this)
//        } else {
//            val bool = Version(versionLimit).isHigherThan(BuildConfig.VERSION_NAME)
//            Log.d("MyLog", "[VERSION_NAME] $bool $versionLimit")
//            openMain()
//        }
//    }

    private fun openMain() {
        prefs.isFirstLunch = false;
        MainActivity.open(this, if (originSite?.length != 0 && originSite != null) originSite else null, fromNotification)
    }

    private fun showDialog(version: String?, lock: Boolean) {
        UpdateDialog.newInstance(this, version, lock).show(supportFragmentManager, UPDATE_DIALOG)
    }

    override fun doPositiveClick() {
        downloadController = DownloadController(this, BuildConfig.URL_APK)
        checkStoragePermission()
    }

    override fun doNegativeClick() {
        openMain()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
