package com.duke.elliot.kim.kotlin.youbroker.Utilities

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.duke.elliot.kim.kotlin.youbroker.R
import kotlinx.android.synthetic.main.fragment_ok_cancel_dialog.view.*

class OkCancelDialogFragment(private val title: String? = null,
                             private val message: String? = null): DialogFragment() {

    private lateinit var okButtonOnClick: () -> Unit

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = requireActivity().layoutInflater.inflate(R.layout.fragment_ok_cancel_dialog, null)

        view.text_title.text = title
        view.text_message.text = message
        view.button_cancel.setOnClickListener {
            this.dismiss()
        }

        view.button_ok.setOnClickListener {
            if (::okButtonOnClick.isInitialized)
                okButtonOnClick.invoke()
        }

        builder.setView(view)
        return builder.create()
    }

    fun setOkButtonOnClick(onClick: () -> Unit) {
        okButtonOnClick = onClick
    }
}