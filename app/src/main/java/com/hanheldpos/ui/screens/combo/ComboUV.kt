package com.hanheldpos.ui.screens.combo

import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.combo.ItemComboGroup
import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.BaseUserView

interface ComboUV : BaseUserView {
    fun onBack();
    fun cartAdded(item : BaseProductInCart, action: ItemActionType);
    fun onLoadComboSuccess(list : List<ItemComboGroup>)
}