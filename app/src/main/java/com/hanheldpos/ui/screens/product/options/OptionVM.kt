package com.hanheldpos.ui.screens.product.options

import com.hanheldpos.model.cart.ModifierCart
import com.hanheldpos.model.cart.VariantCart
import com.hanheldpos.ui.base.BaseUserView
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class OptionVM : BaseUiViewModel<OptionVM.OptionListener>() {

    fun modifierAddItem(item : ModifierCart){
        uiCallback?.onModifierAddItem(item);
    }

    fun modifierRemoveItem(item : ModifierCart){

    }

    fun variantItemChange(item : List<VariantCart>,groupValue : String , priceOverride: Double, sku : String){
        uiCallback?.onVariantItemChange(item,groupValue,priceOverride,sku);
    }

    interface OptionListener : BaseUserView{
        fun onModifierAddItem(item : ModifierCart)
        fun onModifierRemoveItem(item : ModifierCart)
        fun onVariantItemChange(item : List<VariantCart>,groupValue : String , priceOverride: Double, sku : String)
    }
}