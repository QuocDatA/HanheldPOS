package com.hanheldpos.model.product

import com.hanheldpos.model.cart.ModifierCart
import com.diadiem.pos_config.utils.Const
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.cart.VariantCart
import com.hanheldpos.model.order.OrderModifier

object ExtraConverter {
    fun modifierStr(list: List<ModifierCart>): String? {
        return if (list.isEmpty()) null;
        else list.joinToString(separator = Const.SymBol.CommaSeparator) { item ->
             item.name + if (item.quantity > 1) " (${item.quantity})" else ""
        }
    }

    fun modifierOrderStr(list: List<OrderModifier>?): String? {
        return if (list?.isEmpty() == true) null;
        else list?.joinToString(separator = Const.SymBol.CommaSeparator) { item ->
            item.Name + if (item.ModifierQuantity?:0 > 1) " (${item.ModifierQuantity})" else ""
        }
    }

    fun variantStr(list: List<VariantCart>?): String? {
        list ?: return null;
        return list.joinToString(separator = Const.SymBol.CommaSeparator) { item -> item.Value };
    }

}