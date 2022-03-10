package com.hanheldpos.ui.screens.discount.discount_detail

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class DiscountDetailVM : BaseUiViewModel<DiscountDetailUV>() {
    fun backPress() {
        uiCallback?.getBack()
    }

}