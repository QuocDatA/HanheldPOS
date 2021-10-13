package com.hanheldpos.ui.screens.home.order.combo

import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.home.order.combo.ComboItemActionType
import com.hanheldpos.model.home.order.menu.ComboPickedItemViewModel
import com.hanheldpos.model.product.ProductOrderItem
import com.hanheldpos.ui.base.BaseUserView

interface ComboUV : BaseUserView {
    fun onBack();
    fun openProductDetail(maxQuantity : Int ,item : ComboPickedItemViewModel,action: ComboItemActionType?)
    fun cartAdded(item : OrderItemModel)
}