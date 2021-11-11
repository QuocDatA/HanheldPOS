package com.hanheldpos.ui.screens.product.options

import com.hanheldpos.data.api.pojo.order.menu.GroupItem
import com.hanheldpos.data.api.pojo.product.VariantsGroup
import com.hanheldpos.model.cart.VariantCart
import com.hanheldpos.model.product.ExtraDoneModel
import com.hanheldpos.ui.base.BaseUserView
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.base.viewmodel.BaseViewModel
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierSelectedItemModel

class OptionVM : BaseUiViewModel<OptionVM.OptionListener>() {

    fun modifierItemChange(item : ModifierSelectedItemModel){
        uiCallback?.onModifierItemChange(item);
    }
    fun variantItemChange(item : List<VariantCart>,groupValue : String , priceOverride: Double, sku : String){
        uiCallback?.onVariantItemChange(item,groupValue,priceOverride,sku);
    }

    interface OptionListener : BaseUserView{
        fun onModifierItemChange(item : ModifierSelectedItemModel)
        fun onVariantItemChange(item : List<VariantCart>,groupValue : String , priceOverride: Double, sku : String)
    }
}