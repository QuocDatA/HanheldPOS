package com.hanheldpos.ui.screens.home.order

import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class OrderVM : BaseUiViewModel<OrderUV>() {

    private var mLastTimeClick: Long = 0

    fun isValidToProcess(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastTimeClick > 500) {
            Log.d("Space Time", (SystemClock.elapsedRealtime() - mLastTimeClick).toString())
            mLastTimeClick = SystemClock.elapsedRealtime()
            return true
        }
        return false
    }

    fun showCategoryDialog() {
        uiCallback?.showCategoryDialog();
    }

    fun showCart() {
        uiCallback?.showCart();
    }
}