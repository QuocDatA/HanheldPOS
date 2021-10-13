package com.hanheldpos.ui.screens.home.order.combo

import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.home.order.combo.ItemActionType
import com.hanheldpos.model.home.order.menu.ComboPickedItemViewModel
import com.hanheldpos.ui.base.BaseUserView

interface ComboUV : BaseUserView {
    fun onBack();
    fun openProductDetail(maxQuantity : Int ,item : ComboPickedItemViewModel,action: ItemActionType)
    fun cartAdded(item : OrderItemModel)
}