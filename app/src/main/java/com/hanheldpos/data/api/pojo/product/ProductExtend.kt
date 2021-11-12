package com.hanheldpos.data.api.pojo.product

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hanheldpos.data.api.ApiConst
import com.hanheldpos.data.api.pojo.order.menu.getModifierItemByListProduct
import com.hanheldpos.data.api.pojo.order.menu.getProductModifierByListProduct
import com.hanheldpos.data.api.pojo.order.menu.*
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.product.GroupExtra
import com.hanheldpos.model.product.ItemExtra
import com.hanheldpos.model.product.ItemExtraModel
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
 * Modifier
 */

fun ProductItem.getModifierList(orderMenuResp: OrderMenuResp): List<GroupExtra> {
    // TODO: DONE FLOW

    val modifierExtras : List<ModifierExtra> = Gson().fromJson(modifier, object : TypeToken<List<ModifierExtra>?>() {}.type)
    val rs: MutableList<GroupExtra> = mutableListOf()

    modifierExtras.forEach { extraGroup->
        val group = GroupExtra(extraGroup);
        val productModifierList = orderMenuResp.getProductModifierByListProduct()
            ?.find {
                it?.productGuid?.equals(this.id) ?: false && it?.modifierGuid.equals(
                    extraGroup.ModifierGuid
                )
            }
        if(productModifierList != null){
            (Gson().fromJson(modifier, object : TypeToken<List<ModifierExtraID>?>() {}.type) as List<ModifierExtraID>).forEach { modifierId->
                val modifierItem = orderMenuResp.getModifierItemByListProduct()?.find { it.id == modifierId.ModifierItem }
                if (modifierItem != null) {
                    (group.modifierList as MutableList).add(ItemExtra(modifierItem,this))
                }
            }
        }
        else {
            val modifierItemList = orderMenuResp.getModifierItemByListProduct()?.filter {
                it.modifierGuid == extraGroup.ModifierGuid
            }
            modifierItemList?.forEach { modifierItem->
                (group.modifierList as MutableList).add(ItemExtra(modifierItem,this))
            }
        }

    }
    return rs
}



