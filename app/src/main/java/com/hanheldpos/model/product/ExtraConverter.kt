package com.hanheldpos.model.product

import com.diadiem.pos_config.utils.Const
import com.hanheldpos.model.cart.ModifierCart
import com.hanheldpos.model.cart.VariantCart
import com.hanheldpos.model.order.OrderModifier

object ExtraConverter {
    fun modifierStr(list: List<ModifierCart>): String? {
        return if (list.isEmpty()) null
        else list.joinToString(separator = Const.SymBol.CommaSeparator) { item ->
            item.name + (if (item.quantity > 1) " (${item.quantity})" else "")
        }
    }

    fun modifierOrderStr(list: List<OrderModifier>?, separator: String? = null): String? {
        return if (list?.isEmpty() == true) null
        else list?.joinToString(separator = separator ?: Const.SymBol.CommaSeparator) { item ->
            (if ((item.ModifierQuantity
                    ?: 0) > 1
            ) "(${item.ModifierQuantity}) " else "") + item.Name
        }
    }

    fun modifierOrderStr(list: List<OrderModifier>?): String? {
        return if (list?.isEmpty() == true) null
        else list?.joinToString(separator = Const.SymBol.CommaSeparator) { item ->
            (if ((item.ModifierQuantity
                    ?: 0) > 1
            ) "(${item.ModifierQuantity}) " else "") + item.Name
        }
    }

    fun modifierOrderSingleStr(item: OrderModifier?): String? {
        return (if ((item?.ModifierQuantity
                ?: 0) > 1
        ) "(${item?.ModifierQuantity}) " else "") + item?.Name
    }

    fun variantStr(list: List<VariantCart>?): String? {
        list ?: return null
        return list.joinToString(
            separator = Const.SymBol.CommaSeparator
        ) { item -> "(${item.Value})" }
    }

    fun variantStr(list: List<VariantCart>?, separator: String? = null): String? {
        list ?: return null
        return list.joinToString(
            separator = separator ?: Const.SymBol.CommaSeparator
        ) { item -> "(${item.Value})" }
    }

    fun variantOrderStr(list: List<VariantCart>?): String? {
        list ?: return null
        return list.joinToString(
            separator = Const.SymBol.SplashSeparator
        ) { item -> item.Value }
    }

}