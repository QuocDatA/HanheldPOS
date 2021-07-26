package com.hanheldpos.extension

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Created by: thongphm on 2020/11/09 - 14:52
 * Copyright (c) 2020
 */
/**
 * Go to activity
 */
fun <T> Activity.navigateTo(
    activityClass: Class<T>,
    alsoFinishCurrentActivity: Boolean? = false
) {
    startActivity(Intent(this, activityClass))
    if (alsoFinishCurrentActivity == true) {
        finish()
    }
}

/**
 * Go to activity
 */
fun <T> Fragment.navigateTo(
    activityClass: Class<T>,
    alsoFinishCurrentActivity: Boolean? = false
) {
    startActivity(Intent(activity, activityClass))
    if (alsoFinishCurrentActivity == true) {
        activity?.finish()
    }
}

fun BottomNavigationView.disableTooltip() {
    val content: View = getChildAt(0)
    if (content is ViewGroup) {
        content.forEach {
            it.setOnLongClickListener {
                return@setOnLongClickListener true
            }
            // disable vibration also
            it.isHapticFeedbackEnabled = false
        }
    }
}