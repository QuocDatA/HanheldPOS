package com.hanheldpos.model.product

import com.hanheldpos.model.cart.ModifierCart
import com.diadiem.pos_config.utils.Const
import com.hanheldpos.model.cart.VariantCart

object ExtraConverter {
    fun modifierStr(list : List<ModifierCart>) : String? {
        return if (list.isEmpty()) null;
        else list.joinToString(separator = Const.SymBol.CommaSeparator) { item -> (if (item.quantity > 1) "(${item.quantity}) " else "") + item.name + (if (item.price != null && item.price!! > 0.0) " ${item.price.toString()}"  else "")   }
    }

    fun variantStr(list : List<VariantCart>?) : String? {
        list?:return null;
        return  list.joinToString(separator = Const.SymBol.CommaSeparator) { item -> item.Value };
    }
}