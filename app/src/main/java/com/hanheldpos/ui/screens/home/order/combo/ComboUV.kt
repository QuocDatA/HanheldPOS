package com.hanheldpos.ui.screens.home.order.combo

import com.hanheldpos.model.home.order.menu.ComboPickedItemViewModel
import com.hanheldpos.model.product.ProductOrderItem
import com.hanheldpos.ui.base.BaseUserView

interface ComboUV : BaseUserView {
    fun onBack();
    fun openProductDetail(maxQuantity : Int ,item : ComboPickedItemViewModel)
}