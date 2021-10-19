package com.hanheldpos.model.product

import android.os.Parcelable
import android.util.Log
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
data class ProductOrderItem (
    var id: String? = null,
    var text: String? = null,
    var sku: String? = null,
    var description: String? = null,
    var price: Double? = null,
    var isPriceFixed : Boolean? = false,
    var comparePrice: Double? = null,
    var img: String? = null,
    var unitStr: String? = null,
    var mappedItem: ProductItem? = null,
    var extraData: ExtraData? = null,
    var uiType: ProductModeViewType? = null,
    var pricingMethodType: PricingMethodType? = null,
    var modPricingType: ModPricingType? = null,
    var modPricingValue: Double? = null,

    // Combo
    var productComboList: MutableList<ProductComboItem>? = mutableListOf(),
    var listGroupPriceInCombo: MutableList<GroupPriceItem>? = null,

    var maxQuantity: Int = -1,
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

/**
 * Update price product folow GroupPrice of combo
 * @param parent Product Combo Item
 * @param groupBundle The Group Price of Combo Item
 */
fun ProductOrderItem.updatePriceByGroupPrice(
    parent: ProductOrderItem,
    groupBundle: GroupPriceProductItem?
) {

    // Update child folow parent
    this.modPricingType = parent.modPricingType;
    this.modPricingValue = parent.modPricingValue;

    if (groupBundle != null) {
        sku = groupBundle.productSKU;
        price = groupBundle.productAmount;
        if (groupBundle.variants.isNotEmpty()) {
            extraData?.variantStrProductList?.first()?.group?.forEach {
                groupBundle.variants.find { newItem -> newItem.groupID == it?.groupId ?: -1 }
                    .let { findItem ->
                        it?.price = findItem?.groupAmount;
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
    updateModifierPrice();
}

/**
 * Update Modifier Price by Combo ModifierPricingType
 */
fun ProductOrderItem.updateModifierPrice() {
    val modifierPricingValue: Double = modPricingValue ?: 0.0;
    extraData?.modifierMap?.values?.forEach { currentModifierGroup ->
        for (i in 0 until currentModifierGroup.size) {
            val modifierItem = currentModifierGroup[i];
            when (this.modPricingType) {
                ModPricingType.FIX_AMOUNT -> {
                    currentModifierGroup[i] = modifierItem.copy(price = modifierPricingValue);
                    Log.d("CALC MODIFIER", "fix amount ${currentModifierGroup[i].price}");
                }
                ModPricingType.DISCOUNT_AMOUNT -> {
                    val discountPrice = modifierItem.price?.minus(modifierPricingValue)!!;
                    val newPrice =
                        if (discountPrice < 0) 0.0 else discountPrice;
                    currentModifierGroup[i] = modifierItem.copy(price = newPrice);
                    Log.d("CALC MODIFIER", "Discount amount $newPrice")
                }
                ModPricingType.DISCOUNT_PERCENT -> {
                    val newPrice =
                        modifierItem.price!!.times((1.0 - modifierPricingValue / 100));
                    currentModifierGroup[i] = modifierItem.copy(price = newPrice);
                    Log.d("CALC MODIFIER", "Discount percent $newPrice")
                }
                ModPricingType.NONE -> {
                    currentModifierGroup[i] = modifierItem.copy(price = 0.0);
                    Log.d("CALC MODIFIER", "none ${currentModifierGroup[i].price}")
                }
            }
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

