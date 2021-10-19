package com.hanheldpos.ui.screens.home.table

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class TableVM : BaseUiViewModel<TableUV>() {

    var mLastTimeClick: Long = 0

    fun initLifecycle(owner: LifecycleOwner){
        owner.lifecycle.addObserver(this);
    }




}