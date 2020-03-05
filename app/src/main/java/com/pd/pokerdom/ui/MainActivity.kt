package com.pd.pokerdom.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.pd.pokerdom.MainGraphDirections
import com.pd.pokerdom.R
import com.pd.pokerdom.service.FCMService.Companion.KEY_FCM_LINK


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
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
            Log.d("Firebase", "[MainActivity] KEY_LINK: $it")
            navController.navigate(MainGraphDirections.actionGlobalWebFragment(it))
        }
    }

}
