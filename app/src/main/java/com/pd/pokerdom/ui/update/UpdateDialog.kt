package com.pd.pokerdom.ui.update


import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.pd.pokerdom.model.AppVersion
import com.pd.pokerdom.ui.MainActivity

object UpdateDialog : DialogFragment() {

    private const val KEY_APP_VERSION = "AppVersion"

    fun newInstance(version: AppVersion): UpdateDialog {
        val frag = UpdateDialog
        val args = Bundle()
        args.putParcelable(KEY_APP_VERSION, version)
        frag.arguments = args
        return frag
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val version = arguments?.getParcelable<AppVersion>(KEY_APP_VERSION)

        return AlertDialog.Builder(activity!!)
            .setTitle("Новая версия")
//            .setView(R.layout.department_chooser_dialog)
            .setMessage("Доступна новая версия ${version?.version} . Желаете обновить приложение?")
            .setPositiveButton(android.R.string.ok) { d, i ->
                (activity as MainActivity).doPositiveClick()
            }
            .setNegativeButton(android.R.string.cancel) { d, i ->
                d.cancel()
            }
            .create()
    }
}