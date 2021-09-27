package com.hanheldpos.model.product

import android.os.Parcelable
import com.diadiem.pos_config.utils.Const
import com.hanheldpos.data.api.pojo.order.menu.GroupItem
import com.hanheldpos.ui.screens.product.adapter.modifier.ModifierSelectedItemModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExtraDoneModel(
    var productOrderItem: ProductOrderItem? = null,
    var quantity: Int = 1,
    var note: String? = null,
    var itemApplyToType: ItemApplyToType = ItemApplyToType.Normal,
    var selectedVariant: GroupItem? = null,
    // String - key is name of the modifier group
    var selectedModifierGroup: MutableMap<String, LinkedHashSet<ModifierSelectedItemModel>?>? = null,
) : Parcelable {
    fun getName(): String? {
        return productOrderItem?.text
    }
    fun getSku(): String? {
        var sku = productOrderItem?.sku

        selectedVariant?.sku?.let {
            sku = it
        }

        return sku
    }

    fun getPriceOrderItem() : Double{
        return productOrderItem?.price ?: 0.0;
    }

    fun getVariantStr(): String? {
        return getVariantStr(Const.SymBol.SplashSeparator)
    }
    fun getVariantStr(separator: String): String? {
        var rs: String? = null

        selectedVariant?.let {
            rs = it.groupName?.replace(Const.SymBol.VariantSeparator, separator)
        }

        return rs
    }

    fun getModifierStr(): String? {
        return getModifierStr(Const.SymBol.CommaSeparator)
    }

    fun getModifierStr(separator: String) : String? {
        var rs: MutableList<String> = mutableListOf()

        selectedModifierGroup?.forEach { entry ->
            val modifier : String = entry.key +  if (!entry.value.isNullOrEmpty() && entry.value?.sumOf { it.quantity }!! > 1) " (${entry.value?.sumOf { it.quantity }})" else ""
            rs.add(modifier)
        }
        return rs.joinToString(separator = "${separator} ");
    }

    fun getProductGuid(): String? {
        return productOrderItem?.getProductItem()?.id
    }

    fun getDescription(): String {
        return "${productOrderItem?.text} x${quantity}"
    }

    /**
     * Price by variant with Discount Override case
     */
    fun getPriceByVariant(): Double {
        return getPriceByVariant(
            orderItemPrice = this.productOrderItem?.price,
            itemApplyToType = this.itemApplyToType,
            selectedVariant = this.selectedVariant,
        )
    }
}


/**
 *  Use this for Extra Fragment and Extra Done
 */
enum class ItemApplyToType {
    Normal,
    BuyXGetY_BUY,

    /**
     * @BuyXGetY_GET the price will be depend on @DiscountValueType and @DiscountValue inside DiscountCondition
     * right now the flow is not complete show we will set the price of the extra if it is this type = 0
     */
    BuyXGetY_GET,
    Combo
}

/**
 * The price return also check the variant price
 */
fun ExtraDoneModel.getPriceByExtra(): Double {
    return getPriceByExtra(
        orderItemPrice = this.productOrderItem?.price,
        itemApplyToType = this.itemApplyToType,
        quantity = this.quantity,
        selectedVariant = this.selectedVariant,
        groupSelectedModifier = this.selectedModifierGroup,
    )
}

fun getPriceByExtra(
    orderItemPrice: Double?,
    itemApplyToType: ItemApplyToType?,
    quantity: Int,
    selectedVariant: GroupItem?,
    groupSelectedModifier: Map<String, Set<ModifierSelectedItemModel>?>?,
): Double {
    val price = getPriceByVariant(
        orderItemPrice = orderItemPrice,
        itemApplyToType = itemApplyToType,
        selectedVariant = selectedVariant,
    )

    var totalModifier = 0.0
    groupSelectedModifier?.forEach { it ->
        it.value?.forEach {
            val modifierItemPrice = it.realItem?.price?.times(it.quantity) ?: 0.0
            totalModifier += modifierItemPrice
        }
    }

    return quantity.times(price) + totalModifier
}

fun getPriceByVariant(
    orderItemPrice: Double?,
    itemApplyToType: ItemApplyToType?,
    selectedVariant: GroupItem?,
): Double {

    var price = orderItemPrice ?: 0.0
    if (selectedVariant != null) {
        price = selectedVariant.price!!
    }

    if (itemApplyToType == ItemApplyToType.BuyXGetY_GET) {
        price = 0.0
    }

    return price
}