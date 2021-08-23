package com.hanheldpos.ui.screens.home.table

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.screens.notifyValueChange

class TableVM : BaseUiViewModel<TableUV>() {


    fun initLifecycle(owner: LifecycleOwner){
        owner.lifecycle.addObserver(this);
    }



}