package com.pd.pokerdom.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.pd.pokerdom.R
import com.pd.pokerdom.service.FCMService.Companion.KEY_FCM_LINK
import com.pd.pokerdom.service.FCMService.Companion.KEY_FROM_NOTIFICATION
import com.pd.pokerdom.ui.version.VersionActivityFragment
import com.pd.pokerdom.ui.version.VersionControl
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        const val ARG_NOTIFY_LINK = "ARG_NOTIFY_LINK"

        fun open(context: Context, link: String? = null, fromNotification: Boolean = false) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
            )
            fromNotification.let { intent.putExtra(KEY_FROM_NOTIFICATION, it) }
            link?.let { intent.putExtra(KEY_FCM_LINK, it) }
            context.startActivity(intent)
        }
    }
    private val versionControl: VersionControl by viewModel()
    private val viewModel: MainViewModel by viewModel()
    private val navController: NavController by lazy { Navigation.findNavController(this, R.id.nav_host_fragment) }
    private var firstLunch = true
    private var originSite: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (supportActionBar != null) {
//            supportActionBar?.hide();
//        }

        Log.d("Mylog", "[MainActivity]")
        Log.d("testFCMLINK", "onCreate")
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        navController.navigate(R.id.no_connection)
        loadNotification()
    }

    override fun onResume() {
        super.onResume()
        if (!this.firstLunch) {
            val intent = intent.extras
            val isFromNotification: Boolean = intent?.getBoolean(KEY_FROM_NOTIFICATION) ?: false
            if (!isFromNotification) {
                Log.d("testFCMLINK", "onResume")
                val response: Array<Any> = versionControl.getVersion()

//                val errorLimit: Boolean = response[0] as Boolean
                val originSite = response[1] as String
//                if (errorLimit) {
//                    return VersionActivityFragment.open(this)
//                }

                if (originSite != this.originSite) {
                    this.setupGraph(originSite)
                }
            }
        }
    }


    private fun loadNotification() {
        val extra = intent.extras
        val link = extra?.getString(KEY_FCM_LINK)
        Log.d("testFCMLINK", "Link $extra")
        intent.removeExtra(KEY_FCM_LINK)
        link?.let {
            setupGraph(it)
        } ?: defaultUrlLoad()
    }

    private fun defaultUrlLoad() {
        viewModel.configDomain.observe(this, Observer {
            Log.d("testFCMLINK", "defaultUrlLoad $it")
            setupGraph(it)
        })
    }

    private fun setupGraph(it: String) {
        originSite = it
        this.firstLunch = false
        Log.d("testFCMLINK", "setupGraph $it")
        val bundle = Bundle()
        bundle.putString(ARG_NOTIFY_LINK, it)
        val graph = navController.navInflater.inflate(R.navigation.main_graph)
        navController.setGraph(graph, bundle)
    }

}
