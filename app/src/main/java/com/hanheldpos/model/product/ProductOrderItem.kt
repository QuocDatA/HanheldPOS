package com.hanheldpos.model.product

import android.os.Parcelable
import androidx.core.graphics.ColorUtils
import com.hanheldpos.data.api.pojo.order.menu.ModifierItemItem
import com.hanheldpos.data.api.pojo.order.menu.ModifierStrProduct
import com.hanheldpos.data.api.pojo.order.menu.ProductItem
import com.hanheldpos.data.api.pojo.order.menu.VariantStrProduct
import com.hanheldpos.data.api.pojo.product.GroupPriceItem
import com.hanheldpos.data.api.pojo.product.GroupPriceProductItem
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
    var pricingMethodType: PricingMethodType? = null,
    var modPricingType: ModPricingType? = null,
    var modPricingValue: Double?=null,

    // Combo
    var productComboList: MutableList<ProductComboItem>? = mutableListOf(),
    var listGroupPriceInCombo: MutableList<GroupPriceItem>? = null,

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
/*
* Update price product folow GroupPrice of combo
* */
fun ProductOrderItem.updatePriceByGroupPrice(groupBundle : GroupPriceProductItem?){
    if (groupBundle != null) {
        sku = groupBundle.productSKU;
        price = groupBundle.productAmount;
        if (groupBundle.variants.isNotEmpty()) {
            extraData?.variantStrProductList?.first()?.group?.forEach {
                groupBundle.variants.find { newItem -> newItem.groupID == it?.groupId ?: -1 }
                    .let { findedItem ->
                        it?.price = findedItem?.groupAmount;
                    }
            }
        } else {
            extraData?.variantStrProductList?.first()?.group?.forEach {
                it?.price = 0.0;
            }
        }
    } else {
        price = 0.0;

        extraData?.variantStrProductList?.first()?.group?.forEach {
            it?.price = 0.0;
        }
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

