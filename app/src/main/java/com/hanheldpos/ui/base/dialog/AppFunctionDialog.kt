package com.hanheldpos.ui.base.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hanheldpos.R

class AppFunctionDialog : BaseDialog() {

    companion object {

        @Volatile
        private var instance: AppFunctionDialog? = null

        fun get(): AppFunctionDialog =
            instance ?: synchronized(this) {
                val newInstance = instance ?: AppFunctionDialog()
                    .also { instance = it }
                newInstance
            }
    }

    /**
     * Show dialog with custom layout
     */
    fun showCustomLayout(layoutBinding: ViewDataBinding): AlertDialog? {
        context?.let { _context ->
            val dialogBuilder =
                MaterialAlertDialogBuilder(_context, R.style.Base_MaterialAlertDialog)
                    .apply {
                        setCancelable(false)

                        // Custom View
                        setView(layoutBinding.root)
                    }
            if (dialog?.isShowing == true) dialog?.dismiss()
            val customDialog = dialogBuilder.show()

            // Custom background
            val window = customDialog?.window
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            return customDialog
        }
        return null
    }
}