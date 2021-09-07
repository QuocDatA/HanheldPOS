package com.hanheldpos.model.product

import android.os.Parcelable
import com.example.pos2.repo.order.menu.ModifierStrProduct
import com.hanheldpos.data.api.pojo.order.ModifierItemItem
import com.hanheldpos.data.api.pojo.order.VariantStrProduct
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierHeader
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierSelectedItemModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExtraData(
    var variantStrProductList: List<VariantStrProduct>? = null,

    var modifierMap: Map<ModifierStrProduct, MutableList<ModifierItemItem>>? = null
) : Parcelable

fun ExtraData.getDefaultVariantProduct(): VariantStrProduct? {
    return this.variantStrProductList?.firstOrNull()
}

fun ExtraData.getDefaultModifierList(): MutableList<ModifierHeader> {
    val listHeader: MutableList<ModifierHeader> = mutableListOf()
    this.modifierMap?.entries?.forEach { it ->
        listHeader.add(ModifierHeader(
            name = it.key.modifierName,
            childList = it.value.map {
                ModifierSelectedItemModel(it)
            }
        ))
    }

    return listHeader
}
