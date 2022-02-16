package com.hanheldpos.ui.screens.discount

import com.hanheldpos.model.discount.DiscountTypeFor
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountVM : BaseUiViewModel<DiscountUV>() {

    var typeDiscountSelect : DiscountTypeFor?= null;

    fun backPress(){
        uiCallback?.backPress();
    }
}