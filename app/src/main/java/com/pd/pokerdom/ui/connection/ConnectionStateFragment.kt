package com.pd.pokerdom.ui.connection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.pd.pokerdom.R
import com.pd.pokerdom.service.FCMService
import com.pd.pokerdom.ui.main.MainActivity

class ConnectionStateActivity: AppCompatActivity(R.layout.fragment_connection_state) {

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, ConnectionStateActivity::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
            )
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
        Log.d("Mylog", "[ConnectionStateFragment]")
    }
}