package com.hanheldpos.ui.screens.discount

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountVM : BaseUiViewModel<DiscountUV>() {



    fun backPress(){
        uiCallback?.backPress();
    }
}