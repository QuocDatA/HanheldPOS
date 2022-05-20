package com.hanheldpos.ui.screens.cashdrawer

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.hanheldpos.R
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.extension.showWithoutSystemUI
import kotlin.system.exitProcess

object CashDrawerHelper {
    var isStartDrawer : Boolean = false;
    var isEndDrawer: Boolean = false

    fun showDrawerNotification(activity: Activity, isOnStarting: Boolean = true) {
        val builder = AlertDialog.Builder(activity)
        val inflater = LayoutInflater.from(activity)
        val notificationView = inflater.inflate(
            if (isOnStarting) R.layout.layout_notify_started_drawer else R.layout.layout_notify_ended_drawer,
            null
        )

        builder.setView(notificationView)
        val alert = builder.create()

        val close =
            notificationView.findViewById<View>(if (isOnStarting) R.id.btn_close_notification else R.id.btn_start_again)
        close.setOnClickDebounce {
            isStartDrawer = false;
            isEndDrawer = false;
            alert.dismiss()
        }

        if (isOnStarting) {
            val timer = object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}

                override fun onFinish() {
                    alert.dismiss()
                }
            }
            timer.start()
        } else {
            val quit = notificationView.findViewById<View>(R.id.btn_close_app)
            quit.setOnClickDebounce {
                alert.dismiss()
                activity.finishAffinity()
                exitProcess(0);
            }
        }

        alert.setCancelable(false)
        alert.showWithoutSystemUI()
    }
}