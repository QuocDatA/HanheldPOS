package com.hanheldpos.model.buy_x_get_y

import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.order.BuyXGetYEntire
import com.hanheldpos.model.order.ChooseProductApplyTo
import com.hanheldpos.model.order.ProductChosen
import com.hanheldpos.model.product.ProductType


class BuyXGetY() : BaseProductInCart(),Cloneable {

    var groupList: MutableList<GroupBuyXGetY>? = null

    var disc: DiscountResp? = null

    constructor(
        discount: DiscountResp,
        note: String? = null,
        sku: String? = null,
        diningOption: DiningOption,
        quantity: Int?,
        variant: String? = null,
        compReason: Reason? = null,
        discountUserList: MutableList<DiscountUser>?,
        discountServerList: MutableList<DiscountResp>?,
        feeList: MutableList<Fee>? = null,
        groupList: MutableList<GroupBuyXGetY>? = null
    ) : this() {
        this.disc = discount
        this.productType = ProductType.BUYX_GETY_DISC
        this.note = note
        this.sku = sku
        this.diningOption = diningOption
        this.quantity = quantity
        this.variants = variant
        this.compReason = compReason
        this.discountUsersList = discountUserList
        this.discountServersList = discountServerList
        this.fees = feeList
        this.groupList = groupList
    }

    override fun getProductName(): String? {
        return disc?.DiscountName
    }

    override fun getFeeString(): String {
        return String()
    }

    override fun totalFee(): Double {
        return 0.0
    }

    override fun subTotal(): Double {
        return proModSubtotal() * (quantity ?: 0)
    }

    override fun totalDiscount(): Double {
        return 0.0
    }

    override fun total(): Double {
        val totalTemp = subTotal()
        val totalComp = totalComp(totalTemp)
        return totalTemp - totalComp
    }

    private fun totalComp(totalTemp: Double): Double {
        return compReason?.total(totalTemp) ?: 0.0
    }

    override fun totalComp(): Double {
        val totalTemp = subTotal()
        return if (compReason != null) compReason!!.total(totalTemp) else 0.0
    }

    override fun totalPriceUsed(discount: DiscountResp): Double {
        return 0.0
    }

    override fun totalQtyUsed(discount: DiscountResp, product_id: String): Int {
        return 0
    }

    override fun compareValue(productDiscList: List<Product>): Double {
        return price()
    }

    override fun isCompleted(): Boolean {
        if(groupList != null) {
            return groupList?.firstOrNull { gr -> !gr.isCompleted } == null
        }
        return false
    }

    override fun isMatching(productItem: Product): Boolean {
        return false
    }

    override fun clone(): BuyXGetY {
        val cloneValue = BuyXGetY(this.disc!!,
            this.note ,
            this.sku ,
            this.diningOption!! ,
            this.quantity,
            this.variants ,
            this.compReason,
            this.discountUsersList ,
            this.discountServersList ,
            this.fees?.toMutableList(),
            this.groupList )

        cloneValue.variantList = this.variantList?.map { it.copy() }?.toMutableList()
        cloneValue.modifierList.addAll(this.modifierList.map { it.copy() }.toMutableList())
        cloneValue.compReason = this.compReason
        cloneValue.discountUsersList = this.discountUsersList?.map { it.copy() }?.toMutableList()
        cloneValue.discountServersList =
            this.discountServersList?.map { it.copy() }?.toMutableList()
        cloneValue.note = this.note

        return cloneValue
    }

    private fun proModSubtotal(): Double {
        val proSubtotal = price()
        val modSubtotal = 0.0
        return proSubtotal - modSubtotal
    }

    fun price(): Double {
        var total = 0.0
        groupList?.forEach { group -> total += group.productList.sumOf { it.total() } }
        return total
    }

    fun plusOrderQuantity(num: Int) {
        quantity = (quantity ?: 0).plus(num)
    }

    fun minusOrderQuantity(num: Int) {
        quantity = if (quantity!! > 0) quantity!!.minus(num) else 0
    }

    fun toProductChosen(orderDetail_id: Int): ProductChosen {
        var productChosenList = mutableListOf<ProductChosen>()

        var customerGet = disc?.Condition?.CustomerGets
        var customerBuy = disc?.Condition?.CustomerBuys
        var regularsGet = groupList?.last()?.productList

        val price = price()
        val subTotal = subTotal()
        var lineTotal = total()
        var proModSubTotal = price
        var grossPrice = grossPrice(subTotal, 0.0)

        var subOrderDetail_id = 0;

        groupList?.mapIndexed { groupIndex, groupBuyXGetY ->
            groupBuyXGetY.productList.mapIndexed { proIndex, baseProduct ->
                lateinit var subProductChoosed: ProductChosen
                var parentIndex = groupIndex + 1
                if (baseProduct.productType == ProductType.REGULAR) {
                    subProductChoosed = (baseProduct as Regular).toProductChosen(
                        subOrderDetail_id,
                        baseProduct.quantity!!,
                        groupBuyXGetY.groupName,
                        parentIndex
                    )
                } else if (baseProduct.productType == ProductType.BUNDLE) {
                    subProductChoosed = (baseProduct as Combo).toProductChosen(
                        subOrderDetail_id,
                        groupBuyXGetY.groupName,
                        parentIndex
                    )
                }

                subProductChoosed.ProductApplyTo =
                    if (groupIndex == 0) ChooseProductApplyTo.DEFAULT.value else ChooseProductApplyTo.PRO_GET.value
                productChosenList.add(subProductChoosed)

                subOrderDetail_id++
            }
        }

        // CustomerBuy is buy entire
        if (customerBuy?.ApplyTo == CustomerDiscApplyTo.ENTIRE_ORDER.value) {
            subOrderDetail_id++
            val productChosenBuyEntire = ProductChosen(
                orderDetailId = subOrderDetail_id,
                productTypeId = ProductType.UNKNOWN.value,
                parentName = customerBuy.CustomerName,
                productApplyTo = ChooseProductApplyTo.DEFAULT.value,
                buyXGetY = BuyXGetYEntire(
                    ApplyTo = customerBuy.ApplyTo,
                    MinimumTypeId = customerBuy.MinimumTypeId!!,
                    MinimumValue = customerBuy.MinimumValue!!,
                    DiscountValueType = 0,
                    DiscountValue = 0.0,
                )
            )

            productChosenList.add(productChosenBuyEntire)
        }

        // CustomerGet is get entire
        if (customerGet?.ApplyTo == CustomerDiscApplyTo.ENTIRE_ORDER.value) {
            subOrderDetail_id++
            val productChosenGetEntire = ProductChosen(
                orderDetailId = subOrderDetail_id,
                productTypeId = ProductType.UNKNOWN.value,
                parentName = customerGet.CustomerName,
                productApplyTo = ChooseProductApplyTo.PRO_GET.value,
                buyXGetY = BuyXGetYEntire(
                    ApplyTo = customerGet.ApplyTo,
                    MinimumTypeId = 0,
                    MinimumValue = 0.0,
                    DiscountValueType = customerGet.DiscountValueType,
                    DiscountValue = customerGet.DiscountValue,
                ),
            )
            productChosenList.add(productChosenGetEntire)
        }

        val totalCompVoid = totalComp()
        val compVoidList = toCompVoidList(compReason, totalCompVoid)

        // BuyXGetY as ProductChosen
        return ProductChosen(
            orderDetailId = orderDetail_id,
            name1 = disc?.DiscountName,
            _id = disc?._id,
            quantity = quantity,
            subtotal = subTotal,
            lineTotal = lineTotal,
            grossPrice = grossPrice,
            productTypeId = ProductType.BUYX_GETY_DISC.value,
            compVoidList = compVoidList,
            proModSubTotal = proModSubTotal,
            note = note,
            diningOption = diningOption,
            productChosenList = productChosenList,
        )
    }
}