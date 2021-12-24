package com.hanheldpos.ui.screens.discount

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountVM : BaseUiViewModel<DiscountUV>() {

    val itemForDiscount = MutableLiveData<BaseProductInCart>();

    fun backPress(){
        uiCallback?.backPress();
    }
}