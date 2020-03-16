package com.pd.pokerdom.ui.inet

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

object InetDialog : DialogFragment() {

    const val INET_DIALOG = "InetDialog"

    fun newInstance(): InetDialog {
        val frag = InetDialog
//        val args = Bundle()
//        frag.arguments = args
        return frag
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(activity!!)
            .setTitle("Интернет")
            .setMessage("Проверьте подключение")
            .setPositiveButton(android.R.string.ok) { _, _ ->

            }
            .setNegativeButton("Настройки") { _, _ ->
                startActivity(Intent(Settings.ACTION_SETTINGS))
            }
            .create()
    }
}