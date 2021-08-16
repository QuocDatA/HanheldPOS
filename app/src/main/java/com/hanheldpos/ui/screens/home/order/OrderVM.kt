package com.hanheldpos.ui.screens.home.order

import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.CategoryItem
import com.hanheldpos.data.api.pojo.ModelItem
import com.hanheldpos.data.api.pojo.OrderMenuResp
import com.hanheldpos.data.api.pojo.ProductItem
import com.hanheldpos.data.api.pojo.order.getCategoryList
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.screens.notifyValueChange
import kotlinx.coroutines.*

class OrderVM : BaseUiViewModel<OrderUV>() {

    // Value
    var mLastTimeClick: Long = 0

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
    }

    fun changeCategoryPageView(view: View?){
        if (SystemClock.elapsedRealtime() - mLastTimeClick > 300) {
            mLastTimeClick = SystemClock.elapsedRealtime();
            uiCallback?.changeCategoryPageView(view);
        }

    }



}