package com.pd.pokerdom.ui.version

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.pd.pokerdom.R
import com.pd.pokerdom.ui.connection.ConnectionStateActivity

class VersionActivityFragment: AppCompatActivity(R.layout.version_error_page) {

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, VersionActivityFragment::class.java)
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
        Log.d("Mylog", "[VersionActivityFragment]")


        val button: Button = findViewById(R.id.versionButton)

        button.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
            startActivity(browserIntent)
        }
    }
}