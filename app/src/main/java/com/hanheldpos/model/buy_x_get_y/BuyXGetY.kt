package com.hanheldpos.model.buy_x_get_y

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.VariantCart
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.product.ProductType
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class BuyXGetY() : BaseProductInCart(), Parcelable,Cloneable {

    @IgnoredOnParcel
    var groupList: MutableList<GroupBuyXGetY>? = null

    @IgnoredOnParcel
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
}