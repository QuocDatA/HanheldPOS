package com.hanheldpos.ui.screens.product.combo

import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.product.combo.ItemActionType
import com.hanheldpos.model.product.combo.ItemComboGroup
import com.hanheldpos.ui.base.BaseUserView

interface ComboUV : BaseUserView {
    fun onBack()
    fun cartAdded(item : BaseProductInCart, action: ItemActionType)
    fun onLoadComboSuccess(list : List<ItemComboGroup>)
}