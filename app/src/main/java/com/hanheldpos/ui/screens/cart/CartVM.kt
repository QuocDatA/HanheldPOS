package com.hanheldpos.ui.screens.cart

import androidx.lifecycle.LifecycleOwner
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class CartVM : BaseUiViewModel<CartUV>() {

    fun initLifeCycle(owner: LifecycleOwner){
        owner.lifecycle.addObserver(this);
    }

    fun backPress(){
        uiCallback?.getBack();
    }

    fun deleteCart(){
        uiCallback?.deleteCart();
    }

}