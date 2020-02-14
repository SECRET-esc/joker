package com.pd.pokerdom.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.pd.pokerdom.BuildConfig
import com.pd.pokerdom.R
import com.pd.pokerdom.model.AppVersion
import com.pd.pokerdom.ui.update.UpdateDialog
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val versionName: String = BuildConfig.VERSION_NAME
        Log.d("MyTeg", "versionName - $versionName")

        viewModel.getAppVersion()

        viewModel.appVersion.observe(this, Observer { appVersion ->
            val thisVersionName: Int = BuildConfig.VERSION_NAME.replace(".", "").toInt()
            val serverVersionName: Int = appVersion.version.toString().replace(".", "").toInt()
            if (thisVersionName < serverVersionName) {
                showDialog(appVersion)
            }
        })
    }

    private fun showDialog(version: AppVersion) {
        val newFragment = UpdateDialog.newInstance(version)
        newFragment.show(supportFragmentManager, "dialog")
    }

    fun doPositiveClick() { // Do stuff here.
        Toast.makeText(this, "update", Toast.LENGTH_LONG).show()
    }

}