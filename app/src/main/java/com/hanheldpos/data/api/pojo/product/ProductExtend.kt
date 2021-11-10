package com.hanheldpos.data.api.pojo.product

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hanheldpos.data.api.ApiConst
import com.hanheldpos.data.api.pojo.order.menu.getModifierItemByListProduct
import com.hanheldpos.data.api.pojo.order.menu.getProductModifierByListProduct
import com.hanheldpos.data.api.pojo.order.menu.*
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.home.order.ProductModeViewType
import com.hanheldpos.model.image.getImageUrl
import com.hanheldpos.model.product.*
import java.lang.reflect.Type

@SuppressLint("DefaultLocale")
private fun ProductItem.checkValidProduct(): Boolean {
    if (this.visible == ApiConst.IN_VISIBLE)
        return false
    if (location.isBlank())
        return true
    val upCase = location.toUpperCase()
    if (upCase == ApiConst.Location.ALL)
        return true
    if (upCase == ApiConst.Location.NONE)
        return false
    try {
        locationStrFromProduct(this.location)?.forEach {
            if (it.locationGuid == UserHelper.getLocationGui()) {
                return true
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

private fun locationStrFromProduct(string: String?): List<LocationItem>? {
    val listType: Type = object : TypeToken<List<LocationItem>?>() {}.type
    return Gson().fromJson(string, listType)
}

/**
 * Variant
 */

private fun ProductItem.getVariantList(orderMenuResp: OrderMenuResp): List<VariantStrProduct>? {
    if (!this.variants.isNullOrEmpty()) {
        val listType: Type = object : TypeToken<List<VariantStrProduct?>?>() {}.type
        return Gson().fromJson(this.variants, listType)
    }
    return null
}

/**
 * Modifier
 */

private fun ProductItem.getModifierList(orderMenuResp: OrderMenuResp): MutableMap<ModifierStrProduct, MutableList<ModifierItemItem>> {
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



