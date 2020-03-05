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
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        const val ARG_NOTIFY_LINK = "ARG_NOTIFY_LINK"

        fun open(context: Context, link: String? = null) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
            )
            link?.let { intent.putExtra(KEY_FCM_LINK, it) }
            context.startActivity(intent)
        }
    }

    private val viewModel: MainViewModel by viewModel()
    private val navController: NavController by lazy { Navigation.findNavController(this, R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadNotification()
    }

    private fun loadNotification() {
        val extra = intent.extras
        val link = extra?.getString(KEY_FCM_LINK)
        intent.removeExtra(KEY_FCM_LINK)
        link?.let {
            setupGraph(it)
        } ?: defaultUrlLoad()
    }

    private fun defaultUrlLoad() {
        viewModel.configDomain.observe(this, Observer {
            setupGraph(it)
        })
    }

    private fun setupGraph(it: String?) {
        val bundle = Bundle()
        bundle.putString(ARG_NOTIFY_LINK, it)
        val graph = navController.navInflater.inflate(R.navigation.main_graph)
        navController.setGraph(graph, bundle)
    }

}
