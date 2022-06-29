package com.hanheldpos.ui.base.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hanheldpos.R
import com.hanheldpos.extension.showWithoutSystemUI

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
    fun showCustomLayout(
        layoutBinding: ViewDataBinding,
        maxWidth: Double = 1.0,
        maxHeight: Double = 0.5,
    ): AlertDialog? {
        context?.let { _context ->
            val dialogBuilder =
                MaterialAlertDialogBuilder(_context, R.style.Base_MaterialAlertDialog)
                    .apply {
                        setCancelable(true)
                        // Custom View
                        setView(layoutBinding.root)
                    }
            val metrics = DisplayMetrics()
            val wm: WindowManager =
                _context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)

            val maxDialogHeight = (metrics.heightPixels * maxHeight).toInt()

            //Setup dialog params
            val dialogParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            if (layoutBinding.root.measuredHeight >= maxDialogHeight) {
                dialogParams.height = maxDialogHeight
            }
            dialogParams.width = (metrics.widthPixels * maxWidth).toInt()

            if (dialog?.isShowing == true) dialog?.dismiss()
            val customDialog = dialogBuilder.create()
            customDialog.showWithoutSystemUI()
            // Custom background
            val window = customDialog.window
            window?.setLayout(dialogParams.width, dialogParams.height)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            return customDialog
        }
        return null
    }
}