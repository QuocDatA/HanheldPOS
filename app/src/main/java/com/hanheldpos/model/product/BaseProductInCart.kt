package com.hanheldpos.model.product

import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.data.api.pojo.order.settings.ListReasonsItem
import com.hanheldpos.data.api.pojo.product.ProductItem
import com.hanheldpos.model.cart.ModifierCart
import com.hanheldpos.model.cart.VariantCart

abstract class BaseProductInCart {
    open var productType: ProductType = ProductType.NOT_FOUND;
    open var variants: String? = null
    open var sku: String? = null
    open var note: String? = null
    open var quantity: Int? = null
    open var priceOverride : Double? = null
    open var proOriginal: ProductItem? = null
    open var compReason: ListReasonsItem? = null
    open var diningOption: DiningOptionItem? = null
    open var fees : List<Fee>? = null
    val modifierList : MutableList<ModifierCart> = mutableListOf();
    val variantList : MutableList<VariantCart>? = null

    val name get() = getProductName();
    val feeStringValue get() = getFeeString();
    val subtotalValue get() = subTotal();
    val totalDiscountValue get() = totalDiscount();
    val lineTotalValue get() = total();
    val totalCompValue get() = totalComp();

    abstract fun getProductName(): String?
    abstract fun getFeeString(): String
    abstract fun subTotal(): Double;
    abstract fun totalDiscount(): Double;
    abstract fun total(): Double;
    abstract fun totalComp(): Double;

    abstract fun isCompleted() : Boolean;
    abstract fun isMatching(productItem: ProductItem) : Boolean

    open fun grossPrice(subtotal: Double, totalFee: Double): Double {
        val grossPrice = subtotal + totalFee;
        return grossPrice;
    }
}