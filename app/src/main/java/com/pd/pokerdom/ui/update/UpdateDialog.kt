package com.pd.pokerdom.ui.update


import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.pd.pokerdom.model.AppVersion

object UpdateDialog : DialogFragment() {

    private const val KEY_APP_VERSION = "KEY_APP_VERSION"
    private const val KEY_LOCK = "KEY_LOCK"

    private lateinit var listener: IUpdateDialog

    fun newInstance(listener: IUpdateDialog, version: AppVersion, lock: Boolean = false): UpdateDialog {
        val frag = UpdateDialog
        val args = Bundle()
        args.putParcelable(KEY_APP_VERSION, version)
        args.putBoolean(KEY_LOCK, lock)
        frag.arguments = args
        frag.isCancelable = false
        this.listener = listener
        return frag
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val version = arguments?.getParcelable<AppVersion>(KEY_APP_VERSION)
        val lock = arguments!!.getBoolean(KEY_LOCK)

        return AlertDialog.Builder(activity!!)
            .setTitle("Новая версия")
//            .setView(R.layout.department_chooser_dialog)
            .setMessage("Доступна новая версия ${version?.version} \nЖелаете обновить приложение?")
            .setPositiveButton(android.R.string.ok) { d, i ->
                listener.doPositiveClick()
            }
            .setNegativeButton(android.R.string.cancel) { d, i ->
                if (lock) {
                    activity?.finish()
                    return@setNegativeButton
                }
                listener.doNegativeClick()
            }
            .create()
    }
}