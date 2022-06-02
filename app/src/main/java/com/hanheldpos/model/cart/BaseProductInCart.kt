package com.hanheldpos.model.cart

import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.fee.FeeType
import com.hanheldpos.model.discount.DiscountTriggerType
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.order.CompVoid
import com.hanheldpos.model.order.DiscountOrder
import com.hanheldpos.model.order.OrderFee
import com.hanheldpos.model.order.OrderModifier
import com.hanheldpos.model.product.ProductType
import com.hanheldpos.ui.screens.cart.CurCartData
import java.util.*


abstract class BaseProductInCart {
    open var productType: ProductType = ProductType.UNKNOWN
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
    abstract fun total(isGroupBuy: Boolean? = false): Double
    abstract fun totalComp(): Double

    abstract fun totalPriceUsed(discount: DiscountResp): Double
    abstract fun totalQtyUsed(discount: DiscountResp, product_id: String): Int
    abstract fun compareValue(productDiscList: List<Product>): Double

    abstract fun isCompleted(): Boolean
    abstract fun isMatching(productItem: Product): Boolean

    abstract fun totalBuyXGetYDiscount(isGroupBuy: Boolean) : Double

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

    open fun clearAllDiscountCoupon() {
        this.discountServersList?.clear()
        this.discountUsersList?.clear()
    }

    open fun removeAllDiscountCoupon() {
        this.discountServersList?.removeAll { disc ->
            disc.isCoupon()
        }
    }

    open fun removeAllDiscountAutoInCart() {
        this.discountServersList?.removeAll { discount -> discount.isAutoInCart() }
    }

    open fun removeAllDiscountAutoOnClick() {
        this.discountServersList?.removeAll { discount -> discount.isAutoOnClick() }
    }

    open fun addDiscountAutomatic(
        discountAuto: DiscountResp,
        baseProductList: List<BaseProductInCart>
    ) {
        val condition = discountAuto.Condition

        // If discount exists limited quantity.
        // The number of applications will be reduced for subsequent products and must not exceed the total.
        if (condition.CustomerBuys.IsDiscountLimit == 1) {
            val totalPriceUsed =
                baseProductList.sumOf { product -> product.totalPriceUsed(discountAuto) }
            discountAuto.maxAmountUsed =
                (discountAuto.Condition.CustomerBuys.MaximumDiscount) - totalPriceUsed
        }

        // Check if the discount exists in quantity or amount.
        // The allowed amount of the discount must not exceed the allowable limit.
        if (condition.CustomerBuys.IsDiscountLimit == 0 || discountAuto.maxAmountUsed?: 0.0 > 0) {
            if (condition.CustomerBuys.IsMaxQuantity == 1) {
                val totalQtyUsed = baseProductList.sumOf { product ->
                    product.totalQtyUsed(
                        discountAuto,
                        this.proOriginal!!._id
                    )
                }
                val maxQty = discountAuto.Condition.CustomerBuys.getMaxQuantity(
                    totalQtyUsed,
                    this.quantity ?: 0,
                    this.proOriginal!!._id
                ) ?: 0

                if (maxQty > 0) {
                    discountAuto.quantityUsed = maxQty
                    this.addDiscountServer(discountAuto.clone())
                }
            } else {
                this.addDiscountServer(discountAuto.clone())

            }
        }
    }

    fun getOnePerOrderAndUpdateDiscountAutomatic(
        actionType: DiscountTriggerType,
        customer: CustomerResp?,
        baseProductList: List<BaseProductInCart>
    ): List<DiscountResp> {
        if (this.productType != ProductType.BUYX_GETY_DISC) {
            var discountAutoList: List<DiscountResp> = listOf()
            when (actionType) {
                DiscountTriggerType.IN_CART -> {
                    discountAutoList =
                        DataHelper.findDiscountItemList(
                            this,
                            customer,
                            DiscountTriggerType.IN_CART,
                            Date()
                        )
                    removeAllDiscountAutoInCart()
                }
                DiscountTriggerType.ON_CLICK -> {
                    discountAutoList =
                        this.discountServersList?.filter { discount -> discount.isAutoOnClick() }
                            ?.toList() ?: listOf()
                    removeAllDiscountAutoOnClick()
                    discountAutoList = discountAutoList.filter { discount ->
                        discount.isValid(
                            CurCartData.cartModel?.getSubTotal() ?: 0.0,
                            this,
                            customer,
                            Date()
                        )
                    }.toList()
                }
                else -> {}
            }
            addRangeDiscountAutomatic(discountAutoList.filter { disc -> disc.OnlyApplyDiscountProductOncePerOrder == 0 }
                .toList(), baseProductList)
            return discountAutoList.filter { disc ->
                disc.OnlyApplyDiscountProductOncePerOrder == 1
            }.toList()
        }
        return listOf()
    }

    fun addRangeDiscountAutomatic(
        discountList: List<DiscountResp>,
        baseProductList: List<BaseProductInCart>
    ) {
        discountList.forEach { discountAuto -> addDiscountAutomatic(discountAuto, baseProductList) }
    }

    fun totalQtyDiscUsed(discountId: String): Int {
        return discountServersList?.count {
            it._id == discountId
        } ?: 0
    }

    fun isExistDiscount(discountId: String): Boolean {
        return discountServersList?.firstOrNull { discServer -> discServer._id == discountId } != null;
    }

    open fun addDiscountUser(discount: DiscountUser) {
        discountUsersList =
            mutableListOf(discount);
    }

    open fun addDiscountServer(discount: DiscountResp) {
        if (discountServersList == null) {
            discountServersList = mutableListOf(discount)
        } else discountServersList?.find { it._id == discount._id } ?: discountServersList?.add(
            discount
        )
    }

    open fun addCompReason(comp: Reason) {
        compReason = comp;
    }

    open fun clearCompReason() {
        compReason = null
    }

    fun isBuyXGetY() : Boolean {
        return productType == ProductType.BUYX_GETY_DISC
    }
}