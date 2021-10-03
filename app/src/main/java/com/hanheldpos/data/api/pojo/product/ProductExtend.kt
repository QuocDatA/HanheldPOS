package com.hanheldpos.data.api.pojo.product

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hanheldpos.data.api.ApiConst
import com.hanheldpos.data.api.pojo.order.menu.getModifierItemByListProduct
import com.hanheldpos.data.api.pojo.order.menu.getProductModifierByListProduct
import com.hanheldpos.data.api.pojo.order.menu.*
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.home.order.ProductModeViewType
import com.hanheldpos.model.image.getImageUrl
import com.hanheldpos.model.product.ExtraData
import com.hanheldpos.model.product.ProductComboItem
import com.hanheldpos.model.product.ProductOrderItem
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierHeader
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierSelectedItemModel
import kotlinx.android.parcel.Parcelize
import java.lang.reflect.Type

fun ProductItem.toProductOrderItem(
    orderMenuResp: OrderMenuResp
) : ProductOrderItem? {
    var productOrderItem  = ProductOrderItem();
    productOrderItem.mappedItem = this
    if (!this.checkValidProduct()) {
        return null
    }
    productOrderItem.uiType = ProductModeViewType.Product;
    productOrderItem.color = this.color
    productOrderItem.id = this.id
    productOrderItem.text = this.name
    productOrderItem.sku = this.sKU
    productOrderItem.description = this.description
    productOrderItem.price = this.price
    productOrderItem.comparePrice = this.comparePrice
    productOrderItem.unitStr = orderMenuResp.getUnitList()?.find {
        it?.systemUnitId == this.unitType
    }?.abbreviation

    productOrderItem.img = getImageUrl(orderMenuResp, this.id)

    val extraData = ExtraData()
    // Get Variant list
    val variantStrProductList = this.getVariantList(orderMenuResp)
    if (!variantStrProductList.isNullOrEmpty()) {
        extraData.variantStrProductList = variantStrProductList
        productOrderItem.extraData = extraData
    }
    // Get Modifier value
    val modifierList = this.getModifierList(orderMenuResp)
    if (!modifierList.isNullOrEmpty()) {
        extraData.modifierMap = modifierList
        productOrderItem.extraData = extraData
    }
    productOrderItem.extraData = extraData
    //Get Combo list
    //Get ProductCombo list
    val productComboList = getProductComboList(combo)
    if (!productComboList.isNullOrEmpty()) {
        productOrderItem.productComboList = productComboList
        productOrderItem.uiType = ProductModeViewType.Combo
    }
    if (this.groupPrices != null){
        productOrderItem.listGroupPriceInCombo = this.groupPrices?.toMutableList();
    }



    return productOrderItem;
}


/**
 * Get ProductCombo list by parse String model from api server
 */
private fun getProductComboList(comboStr: String?): MutableList<ProductComboItem> {
    val listType: Type = object : TypeToken<List<ProductComboItem>?>() {}.type
    return Gson().fromJson(comboStr, listType)
}

@SuppressLint("DefaultLocale")
private fun ProductItem.checkValidProduct(): Boolean {
    if (this.visible == ApiConst.IN_VISIBLE)
        return false
    if (location.isNullOrBlank())
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


