package com.pd.pokerdom.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.pd.pokerdom.R


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
            )
            context.startActivity(intent)
        }
    }

    private val navController: NavController by lazy { Navigation.findNavController(this, R.id.nav_host_fragment) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}
