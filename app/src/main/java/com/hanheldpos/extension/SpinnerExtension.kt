package com.hanheldpos.extension

import android.annotation.SuppressLint
import android.widget.PopupWindow
import android.widget.Spinner

@SuppressLint("DiscouragedPrivateApi")
fun Spinner.avoidDropdownFocus() {
    try {
        val isAppCompat = this is androidx.appcompat.widget.AppCompatSpinner
        val spinnerClass =
            if (isAppCompat) androidx.appcompat.widget.AppCompatSpinner::class.java else Spinner::class.java
        val popupWindowClass =
            if (isAppCompat) androidx.appcompat.widget.ListPopupWindow::class.java else android.widget.ListPopupWindow::class.java

        val listPopup = spinnerClass
            .getDeclaredField("mPopup")
            .apply { isAccessible = true }
            .get(this)
        if (popupWindowClass.isInstance(listPopup)) {
            val popup = popupWindowClass
                .getDeclaredField("mPopup")
                .apply { isAccessible = true }
                .get(listPopup)
            if (popup is PopupWindow) {
                popup.isFocusable = false
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}