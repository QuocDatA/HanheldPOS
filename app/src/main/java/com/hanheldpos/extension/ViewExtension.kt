package com.hanheldpos.extension

import android.os.SystemClock
import android.view.View

class OnSingleClickListener(
    private val timeDisableInMillisecond: Int,
    private val block: () -> Unit
) : View.OnClickListener {

    private var lastClickTime = 0L

    override fun onClick(view: View) {
        if (SystemClock.elapsedRealtime() - lastClickTime < timeDisableInMillisecond) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()

        block()
    }
}

fun View.setOnClickDebounce(timeDelayInSecond: Int = 500, block: () -> Unit) {
    setOnClickListener(OnSingleClickListener(timeDelayInSecond, block))
}