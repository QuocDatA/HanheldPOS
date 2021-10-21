package com.hanheldpos.ui.base.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.CaseMap
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.DialogMessageBinding

class AppAlertDialog : BaseDialog() {

    companion object {

        @Volatile
        private var instance: AppAlertDialog? = null

        fun get(): AppAlertDialog =
            instance ?: synchronized(this) {
                val newInstance = instance ?: AppAlertDialog()
                    .also { instance = it }
                newInstance
            }
    }

    /**
     * Show with title, message & positive button
     *
     *
     * ------------------------------------------
     * | Title                                  |
     * | Message                                |
     * ------------------------POSITIVE BUTTON---
     */
    @JvmOverloads
    fun show(
        title: String? = null,
        message: String?,
        positiveText: String? = null,
        negativeText: String? = null,
        onClickListener: AlertDialogOnClickListener? = null
    ) {
        context?.let { context ->
            AlertDialog.Builder(context, R.style.Base_MaterialAlertDialog)
                .setCancelable(false)
                .apply {
                    // Custom View
                    val binding = DataBindingUtil.inflate<DialogMessageBinding>(
                        LayoutInflater.from(context),
                        R.layout.dialog_message,
                        null, true
                    )
                    setView(binding.root)

                    binding.apply {
                        this.message = message
                        this.title = title

                        // Confirm button
                        this.btnConfirmText = positiveText ?: context.getString(android.R.string.ok)
                        this.setBtnConfirmOnClick {
                            dismiss()
                            onClickListener?.onPositiveClick()
                        }

                        // Denied button
                        this.btnDeniedText = negativeText
                        this.setBtnDeniedOnClick {
                            dismiss()
                            onClickListener?.onNegativeClick()
                        }
                    }

                    create()
                        .apply {
                            setCanceledOnTouchOutside(false)
                        }
                        .also {
                            dialog = it
                            dismiss()

                            if ((context as Activity).isFinishing) return
                            dialog!!.show()

                            // Custom background
                            val window = dialog!!.window
                            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        }
                }
        }
    }

    interface AlertDialogOnClickListener {
        fun onPositiveClick()
        fun onNegativeClick() = Unit
    }
}