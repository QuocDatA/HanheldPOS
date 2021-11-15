package com.hanheldpos.ui.screens.combo

import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.combo.ItemComboGroup
import com.hanheldpos.ui.base.BaseUserView

interface ComboUV : BaseUserView {
    fun onBack();
    fun cartAdded(item : OrderItemModel, action: ItemActionType);
    fun onLoadComboSuccess(list : List<ItemComboGroup>)
}