package com.hanheldpos.ui.screens.discount

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountVM : BaseUiViewModel<DiscountUV>() {

    var typeDiscountSelect = MutableLiveData<DiscountTypeFor>();

    fun backPress(){
        uiCallback?.backPress();
    }
}