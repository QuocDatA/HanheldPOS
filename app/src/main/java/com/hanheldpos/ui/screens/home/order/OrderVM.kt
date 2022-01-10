package com.hanheldpos.ui.screens.home.order

import android.os.SystemClock
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class OrderVM : BaseUiViewModel<OrderUV>() {

    // Value

    var mLastTimeClick: Long = 0

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
    }

    fun changeCategoryPageView(view: View?){
        if (SystemClock.elapsedRealtime() - mLastTimeClick > 300) {
            mLastTimeClick = SystemClock.elapsedRealtime();

        }

    }

    fun showCategoryDialog(){
        uiCallback?.showCategoryDialog(isBackToTable = false);
    }

    fun showCart(){
        uiCallback?.showCart();
    }
}