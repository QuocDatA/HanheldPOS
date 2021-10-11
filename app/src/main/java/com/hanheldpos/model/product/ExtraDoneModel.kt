package com.hanheldpos.model.product

import android.os.Parcelable
import com.diadiem.pos_config.utils.Const
import com.hanheldpos.data.api.pojo.order.menu.GroupItem
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
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

    var diningOption: DiningOptionItem? = null
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

    fun getPriceOrderItem(): Double {
        val price: Double =
            if (selectedVariant != null) {
                selectedVariant!!.price!!;
            } else {
                productOrderItem?.price ?: 0.0;
            }
        return price;
    }

    fun getVariantStr(): String? {
        return getVariantStr(Const.SymBol.CommaSeparator)
    }

    fun getVariantStr(separator: String): String? {
        var rs: String? = null

        selectedVariant?.let {
            rs = it.groupName?.replace(Const.SymBol.VariantSeparator, separator + " ")
        }

        return rs
    }

    fun getModifierStr(): String? {
        return getModifierStr(Const.SymBol.CommaSeparator)
    }

    fun getModifierStr(separator: String): String? {
        var rs: MutableList<String> = mutableListOf()

        selectedModifierGroup?.forEach { entry ->
            val modifier: String =
                entry.key + if (!entry.value.isNullOrEmpty() && entry.value?.sumOf { it.quantity }!! > 1) " (${entry.value?.sumOf { it.quantity }})" else ""
            rs.add(modifier)
        }
        return rs.joinToString(separator = "$separator ");
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
 *  [Normal] Regular item
 *  [Combo] Combo item
 *  [BuyXGetY_BUY]
 *  [BuyXGetY_GET]
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
 * The price return LineSubTotal
 */


fun ExtraDoneModel.getPriceLineTotal(): Double {
    val subtotal = getPriceSubTotal();
    return subtotal;
}

fun ExtraDoneModel.getPriceModSubTotal(): Double {
    return getPriceByModifier(
        groupSelectedModifier = this.selectedModifierGroup,
    )
}

fun ExtraDoneModel.getPriceProModSubTotal(): Double {
    val price = getPriceRegular();
    return price + getPriceModSubTotal()
}

fun ExtraDoneModel.getPriceSubTotal(): Double {
    return getPriceProModSubTotal() * this.quantity;
}

private fun ExtraDoneModel.getPriceRegular(): Double {
    return getPriceByVariant(
        orderItemPrice = this.productOrderItem?.price,
        itemApplyToType = itemApplyToType,
        selectedVariant = selectedVariant,
    )
}

private fun ExtraDoneModel.getPriceByModifier(
    groupSelectedModifier: Map<String, Set<ModifierSelectedItemModel>?>?,

    ): Double {
    var totalModifier = 0.0
    var totalQuantityMod = 0;
    groupSelectedModifier?.forEach { it ->
        it.value?.forEach {
            totalQuantityMod += it.quantity;
            val modifierItemPrice = it.realItem?.price?.times(it.quantity) ?: 0.0
            totalModifier += modifierItemPrice
        }
    }
    return totalModifier
}

private fun getPriceByVariant(
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


