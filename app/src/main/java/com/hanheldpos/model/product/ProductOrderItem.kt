package com.hanheldpos.model.product

import android.os.Parcelable
import androidx.core.graphics.ColorUtils
import com.hanheldpos.data.api.pojo.order.menu.ModifierItemItem
import com.hanheldpos.data.api.pojo.order.menu.ModifierStrProduct
import com.hanheldpos.data.api.pojo.order.menu.ProductItem
import com.hanheldpos.data.api.pojo.order.menu.VariantStrProduct
import com.hanheldpos.model.home.order.ProductModeViewType
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierHeader
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierSelectedItemModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductOrderItem(
    var id: String? = null,
    var text: String? = null,
    var sku: String? = null,
    var description: String? = null,
    var price: Double? = null,
    var comparePrice: Double? = null,
    var img: String? = null,
    var unitStr: String? = null,
    var mappedItem : Parcelable? = null,
    var extraData: ExtraData? = null,
    var maxQuantity:Int = -1,
    var uiType : ProductModeViewType? = null,
    // Combo
    var productComboList: MutableList<ProductComboItem>? = mutableListOf(),
    var listAllItemsInCombo: MutableList<ProductOrderItem>? = null,
) : Parcelable, Cloneable {
    @IgnoredOnParcel
    var color: String? = null;
    fun getProductItem(): ProductItem? {
        if (mappedItem is ProductItem) {
            return mappedItem as ProductItem
        }
        return null
    }
}

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

