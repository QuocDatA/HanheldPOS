package com.hanheldpos.model.cart

import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.fee.FeeType
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.order.*
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.ui.screens.cart.CartDataVM
import com.hanheldpos.ui.screens.cart.CurCartData


abstract class BaseProductInCart {
    open var productType: ProductType = ProductType.NOT_FOUND
    open var variants: String? = null
    open var sku: String? = null
    open var note: String? = null
    open var quantity: Int? = null
    open var proOriginal: Product? = null
    open var compReason: Reason? = null
    open var diningOption: DiningOption? = null
    open var fees: List<Fee>? = null
    var modifierList: MutableList<ModifierCart> = mutableListOf()
    var variantList: MutableList<VariantCart>? = null
    var discountUsersList: MutableList<DiscountUser>? = null
    var discountServersList: MutableList<DiscountResp>? = null
    val name get() = getProductName()
    val feeStringValue get() = getFeeString()
    val subtotalValue get() = subTotal()
    val totalDiscountValue get() = totalDiscount()
    val lineTotalValue get() = total()
    val totalCompValue get() = totalComp()
    val priceOverride
        get() = proOriginal?.priceOverride(
            CurCartData.cartModel?.menuLocationGuid,
            sku,
            0.0
        ) ?: 0.0

    abstract fun getProductName(): String?
    abstract fun getFeeString(): String
    abstract fun totalFee(): Double
    abstract fun subTotal(): Double
    abstract fun totalDiscount(): Double
    abstract fun total(): Double
    abstract fun totalComp(): Double

    abstract fun isCompleted(): Boolean
    abstract fun isMatching(productItem: Product): Boolean

    abstract fun clone(): BaseProductInCart

    open fun grossPrice(subtotal: Double, totalFee: Double): Double {
        val grossPrice = subtotal + totalFee
        return grossPrice
    }

    open fun toModifierList(
        modifierCarts: List<ModifierCart>,
        proOriginal: Product
    ): List<OrderModifier> {
        val modifierOrders = mutableListOf<OrderModifier>()

        modifierCarts.forEach { modifierCart ->
            val pricingPrice = modifierCart.pricing(proOriginal)

            val modifier = OrderModifier(
                ModifierItemGuid = modifierCart.modifierId,
                Name = modifierCart.name,
                ModifierQuantity = modifierCart.quantity,
                PricingMethodId = proOriginal.PricingMethodType,
                DiscountValue = proOriginal.ModifierPricingValue,
                PriceOriginal = modifierCart.price,
                Price = pricingPrice,
                ModifierSubtotal = pricingPrice.times(modifierCart.quantity),
                DiscountTotalPrice = modifierCart.total(pricingPrice)
            )
            modifierOrders.add(modifier)
        }
        return modifierOrders
    }

    open fun toOrderDiscountList(
        discountServers: List<DiscountResp>?,
        discountUsers: List<DiscountUser>?,
        proSubtotal: Double,
        modSubtotal: Double,
        productOriginal_id: String? = null,
        quantity: Int? = 1
    ): List<DiscountOrder> {
        val discountOrders = (discountServers?.map { disc ->
            DiscountOrder(
                disc,
                proSubtotal,
                modSubtotal,
                productOriginal_id,
                quantity ?: 0
            )
        } ?: listOf()).plus(discountUsers?.map { disc ->
            DiscountOrder(
                disc,
                proSubtotal,
                modSubtotal,
                productOriginal_id,
                quantity ?: 0
            )
        } ?: listOf())

        return discountOrders

    }

    open fun toCompVoidList(reason: Reason?, totalPrice: Double?): List<CompVoid> {
        val compVoids = mutableListOf<CompVoid>()
        reason ?: return compVoids
        val parentId = DataHelper.orderSettingLocalStorage?.ListVoid?.firstOrNull()?.Id
        val compVoid = CompVoid(reason, parentId, totalPrice)
        compVoids.add(compVoid)
        return compVoids
    }

    open fun toOrderFeeList(
        fees: List<Fee>,
        feeType: FeeType,
        subtotal: Double?,
        totalDiscounts: Double?
    ): List<OrderFee> {
        return fees.filter { f -> FeeType.fromInt(f.FeeTypeId) == feeType }
            .map { fee -> OrderFee(fee, subtotal ?: 0.0, totalDiscounts ?: 0.0) }
    }

}