package com.hanheldpos.model.cart

import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.discount.DiscountServer
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.ui.screens.cart.CurCartData


abstract class BaseProductInCart  {
    open var productType: ProductType = ProductType.NOT_FOUND;
    open var variants: String? = null
    open var sku: String? = null
    open var note: String? = null
    open var quantity: Int? = null
    open var proOriginal: Product? = null
    open var compReason: Reason? = null
    open var diningOption: DiningOption? = null
    open var fees : List<Fee>? = null
    val modifierList : MutableList<ModifierCart> = mutableListOf();
    var variantList : MutableList<VariantCart>? = null
    var discountUsersList : MutableList<DiscountUser>? = null
    var discountServersList : MutableList<DiscountServer>? = null
    val name get() = getProductName();
    val feeStringValue get() = getFeeString();
    val subtotalValue get() = subTotal();
    val totalDiscountValue get() = totalDiscount();
    val lineTotalValue get() = total();
    val totalCompValue get() = totalComp();
    val priceOverride get() = proOriginal?.priceOverride(CurCartData.cartModelLD.value?.menuLocationGuid,sku,0.0) ?: 0.0
    abstract fun getProductName(): String?
    abstract fun getFeeString(): String
    abstract fun totalFee() : Double;
    abstract fun subTotal(): Double;
    abstract fun totalDiscount(): Double;
    abstract fun total(): Double;
    abstract fun totalComp(): Double;

    abstract fun isCompleted() : Boolean;
    abstract fun isMatching(productItem: Product) : Boolean

    abstract fun clone() : BaseProductInCart

    open fun grossPrice(subtotal: Double, totalFee: Double): Double {
        val grossPrice = subtotal + totalFee;
        return grossPrice;
    }




}