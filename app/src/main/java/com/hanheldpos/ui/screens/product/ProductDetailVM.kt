package com.hanheldpos.ui.screens.product

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class ProductDetailVM : BaseUiViewModel<ProductDetailUV>() {

    //Value
    val isToolbarExpand = MutableLiveData<Boolean>(true);



    fun initLifeCycle(owner: LifecycleOwner){
        owner.lifecycle.addObserver(this);
    }

    fun goBack()
    {
        uiCallback?.goBack();
    }
}