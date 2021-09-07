package com.hanheldpos.data.api.pojo.product

import com.example.pos2.repo.order.menu.ModifierStrProduct
import com.example.pos2.repo.order.menu.ModifierStrProductModifier
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hanheldpos.data.api.pojo.order.*
import java.lang.reflect.Type

/**
 * Variant
 */

fun ProductItem.getVariantList(orderMenuResp: OrderMenuResp): List<VariantStrProduct>? {
    if (!this.variants.isNullOrEmpty()) {
        val listType: Type = object : TypeToken<List<VariantStrProduct?>?>() {}.type
        return Gson().fromJson(this.variants, listType)
    }
    return null
}

/**
 * Modifier
 */

fun ProductItem.getModifierList(orderMenuResp: OrderMenuResp): MutableMap<ModifierStrProduct, MutableList<ModifierItemItem>> {
    // TODO: DONE FLOW

    val rs: MutableMap<ModifierStrProduct, MutableList<ModifierItemItem>> = mutableMapOf()

    if (!this.modifier.isNullOrEmpty()) {
        val modifierByStrProductList: List<ModifierStrProduct>? =
            modifierStrFromProduct(this.modifier)

        modifierByStrProductList?.forEach { it ->
            val modifierByProduct = it

            val modifierCustom = orderMenuResp.getProductModifierByListProduct()
                ?.find {
                    it?.productGuid?.equals(this.id) ?: false && it?.modifierGuid.equals(
                        modifierByProduct.modifierGuid
                    )
                }

            val modifierItemByListProduct = orderMenuResp.getModifierItemByListProduct()

            // check custom first if have custom then get the modifier by the custom modifier
            if (modifierCustom != null) {
                val modifierCustomList = modifierStrFromProductModifier(modifierCustom.modifierItem)

                modifierCustomList?.forEach { it ->
                    val modifierItemCustom = it  // get form Modifier Item list by Modifier custom

                    val modifierItem = modifierItemByListProduct?.find {
                        it?.id == modifierItemCustom.modifierItem
                    }

                    modifierItem?.let {
                        if (!rs.containsKey(modifierByProduct)) {
                            rs[modifierByProduct] = mutableListOf()
                        }
                        rs[modifierByProduct]?.add(it)
                    }

                }
            } else {
                modifierItemByListProduct?.filter {
                    it?.modifierGuid == modifierByProduct.modifierGuid
                }?.forEach {
                    it?.let {
                        if (!rs.containsKey(modifierByProduct)) {
                            rs[modifierByProduct] = mutableListOf()
                        }
                        rs[modifierByProduct]?.add(it)
                    }
                }
            }

        }
    }

    return rs
}

private fun modifierStrFromProduct(string: String?): List<ModifierStrProduct>? {
    val listType: Type = object : TypeToken<List<ModifierStrProduct>?>() {}.type
    return Gson().fromJson(string, listType)
}

private fun modifierStrFromProductModifier(string: String?): List<ModifierStrProductModifier>? {
    val listType: Type = object : TypeToken<List<ModifierStrProductModifier>?>() {}.type
    return Gson().fromJson(string, listType)
}