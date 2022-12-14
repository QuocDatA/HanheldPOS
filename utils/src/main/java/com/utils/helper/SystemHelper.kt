package com.utils.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlin.system.exitProcess
import android.view.WindowManager
import androidx.core.view.*


object SystemHelper {

    /**
     * Hide status bar & navigation bar
     */
    fun hideSystemUI(window: Window) {
        window.run {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_IMMERSIVE)
            } else {
                setDecorFitsSystemWindows(true)
                insetsController?.let { controller ->
                    controller.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    controller.hide(
                        WindowInsets.Type.systemBars()
                    )
                }
            }
        }

    }

    /**
     * Show or Hide keyboard
     */
    fun toggleSoftInput(context: Context?, show: Boolean) {
        val inputManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        (context as? Activity)?.window?.let { window ->
            if (show) {
                inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
                window.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                            or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                )
            } else {
                window.setSoftInputMode(
                    (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                            or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                            )
                )
                context.currentFocus?.let { view ->
                    inputManager.hideSoftInputFromWindow(
                        view.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                }
            }
        }
    }

    /**
     * Show or Hide keyboard
     */
    fun toggleSoftInput(editText: EditText, show: Boolean) {
        (editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .apply {
                if (show) {
                    toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                } else {
                    hideSoftInputFromWindow(editText.windowToken, 0)
                }
            }
    }

    /**
     * Get display size
     */
    fun getDisplaySize(displayMetrics: DisplayMetrics): Point {
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        return Point(width, height)
    }

    /**
     * Restart app
     */
    fun <C> restartApp(context: Context?, mainClass: Class<C>) {
        context?.run {
            startActivity(Intent(this, mainClass).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
            exitProcess(0)
        }
    }
}