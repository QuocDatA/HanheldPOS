package com.hanheldpos.model.cart

import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.discount.CouponDiscountReq
import com.hanheldpos.data.api.pojo.discount.DiscountCoupon
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.model.discount.DiscountPriceType
import com.hanheldpos.model.discount.DiscountTriggerType
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.home.table.TableSummary
import com.hanheldpos.model.order.DeliveryTime
import com.hanheldpos.model.order.Order
import com.hanheldpos.model.order.Shipping
import com.hanheldpos.model.payment.PaymentOrder
import java.util.*

open class CartModel(
    var order: Order? = null,
    var table: TableSummary,
    var customer: CustomerResp? = null,
    var shipping: Shipping? = null,
    var diningOption: DiningOption,
    var deliveryTime: DeliveryTime? = null,
    val fees: List<Fee>,
    var paymentsList: MutableList<PaymentOrder>? = null,
    var productsList: MutableList<BaseProductInCart>,
    var discountUserList: MutableList<DiscountUser>,
    var discountServerList: MutableList<DiscountResp>,
    var compReason: Reason? = null,
    var createDate: String? = null,
    var orderCode: String? = null,
    var orderGuid: String? = null,
    var menuLocationGuid: String? = null,
    var note: String? = null,
) {

    fun getSubTotal() = productsList.sumOf {
        it.lineTotalValue
    }

    fun getDiscountPrice() = totalDiscount(getSubTotal());

    fun getTotalQuantity() = productsList.sumOf {
        it.quantity ?: 0
    }

    fun getTotalPrice() = total();

    fun updatePriceList(menuLocation_id: String) {
        this.menuLocationGuid = menuLocation_id;
    }

    fun totalDiscount(subTotal: Double): Double {
        val totalDiscUser = discountUserList.sumOf { it.total(subTotal) };
        val totalDiscServer = discountServerList.sumOf { it.total(subTotal, 0.0) ?: 0.0 }
        val total = totalDiscUser + totalDiscServer;
        return total;
    }

    fun anyProductList(): Boolean {
        return !this.productsList.isNullOrEmpty() && this.productsList.any()
    }

    fun totalFee(subTotal: Double, totalDiscount: Double): Double {
        return fees.sumOf { fee ->
            fee.price(subTotal, totalDiscount)
        }
    }

    fun totalGross(subTotal: Double, totalDiscount: Double): Double {
        return subTotal + totalDiscount;
    }

    fun totalTemp(): Double {
        val totalDiscPrice = totalDiscount(getSubTotal());
        val totalFeePrice = totalFee(getSubTotal(), totalDiscPrice);
        var total = getSubTotal() + totalFeePrice - totalDiscPrice;
        total = if (total < 0) 0.0 else total;
        return total;
    }

    fun totalComp(totalTemp: Double): Double {
        val comp = compReason?.total(totalTemp);
        return comp ?: 0.0;
    }

    fun totalComp(): Double {
        val totalTemp = totalTemp();
        val comp = compReason?.total(totalTemp);
        return comp ?: 0.0;
    }

    fun total(): Double {
        val totalTemp = totalTemp();
        val totalComp = totalComp(totalTemp);

        val lineTotal = totalTemp - totalComp;
        return lineTotal
    }

    fun addRegular(regular: Regular) {
        val listFee = OrderHelper.findFeeProductList(regular.proOriginal!!._id);
        regular.fees = listFee;
        productsList.add(regular);
    }

    fun addBundle(combo: Combo) {
        val listFee = OrderHelper.findFeeProductList(combo.proOriginal!!._id);
        combo.fees = listFee;
        productsList.add(combo);
    }

    fun addCompReason(reason: Reason) {
        compReason = reason;
    }

    fun addDiscountUser(discount: DiscountUser) {
        discountUserList = mutableListOf(discount);
    }

    fun addDiscountAutoServer(discount: DiscountResp, discApplyTo: DiscApplyTo) {
        when (discApplyTo) {
            DiscApplyTo.UNKNOWN -> {}
            DiscApplyTo.ITEM -> {
                addDiscountAutoOnClick(discount)
            }
            DiscApplyTo.ORDER -> {

            }
        }
    }

    private fun addDiscountAutoOnClick(discount: DiscountResp) {
        if (discount.OnlyApplyDiscountProductOncePerOrder == 1) {
            addDiscountOnePerOrder(discount)
        }
        productsList.forEach { baseProductInCart ->
            if (discount.isValid(
                    getSubTotal(),
                    baseProductInCart,
                    customer,
                    Date()
                ) && !baseProductInCart.isExistDiscount(discountId = discount._id)
            )
                baseProductInCart.addDiscountAutomatic(discount, productsList);
        }
    }

    fun addDiscountCouponServer(discount: DiscountCoupon, discApplyTo: DiscApplyTo , productApply : BaseProductInCart? = null) {
        when(discApplyTo) {
            DiscApplyTo.UNKNOWN -> TODO()
            DiscApplyTo.ITEM -> {
                if (productApply != null && productApply.discountServersList == null)
                    productApply.discountServersList = mutableListOf()
                addDiscountCouponCode(productApply?.discountServersList,discount)
            }
            DiscApplyTo.ORDER -> {
                addDiscountCouponCode(discountServerList,discount)
            }
        }

    }

    private fun addDiscountCouponCode(
        discountServerList : MutableList<DiscountResp>?,
        disc: DiscountCoupon
    ) {
        val discountApply = DataHelper.findDiscount(discountId = disc.DiscountGuid)?.copy()
        //Update discount
        if (discountApply != null) {
            discountApply.maxAmountUsed = disc.DiscountLineTotalPrice;
            discountApply.quantityUsed = disc.Quantity ?: 1;
            discountApply.DiscountCode = disc.DiscountCode;
            discountServerList?.add(discountApply)
        }
    }

    fun addPayment(payment: List<PaymentOrder>) {
        paymentsList = payment.toMutableList();
    }

    fun clearCart() {
        productsList.clear();
        customer = null;
    }

    fun clearAllDiscounts() {
        this.discountUserList.clear()
        removeAllDiscountAutoInCart()
        removeAllDiscountAutoOnClick()

        this.productsList.forEach { product ->
            product.clearAllDiscountCoupon()
        }
    }

    fun removeAllDiscountAutoInCart() {
        this.discountServerList.removeAll { disc ->
            disc.isAutoInCart()
        }
    }

    fun removeAllDiscountAutoOnClick() {
        this.discountServerList.removeAll { disc ->
            disc.isAutoOnClick()
        }
    }

    fun updateDiscount(isRemoveCoupon: Boolean) {
        //update discount coupon code
        if (isRemoveCoupon) {
            removeAllDiscountCoupon()
        }
        updateDiscountAutomatic(DiscountTriggerType.IN_CART)
        updateDiscountAutomatic(DiscountTriggerType.ON_CLICK)
    }

    fun updateDiscountAutomatic(triggerType: DiscountTriggerType) {

        if (!anyProductList()) return
        var discountAutoList: List<DiscountResp> = listOf()
        when (triggerType) {
            DiscountTriggerType.IN_CART -> {
                discountAutoList =
                    DataHelper.findDiscountOrderList(this, Date(), DiscountTriggerType.IN_CART)
                removeAllDiscountAutoInCart()
            }
            DiscountTriggerType.ON_CLICK -> {
                discountAutoList = this.discountServerList.filter { disc ->
                    disc.isAutoOnClick()
                }.toList()
                removeAllDiscountAutoOnClick()
                discountAutoList = discountAutoList.filter { disc ->
                    disc.isValid(this, Date())
                }.toList()
            }
            else -> {}
        }
        this.discountServerList.addAll(discountAutoList)

        // OnlyApplyDiscountProductOncePerOrder will tell us
        // if this discount has to be applied 1 time per order.

        val discOnePerOrderList: MutableList<DiscountResp> = mutableListOf()
        this.productsList.forEach { baseProduct ->
            val discList = baseProduct.getOnePerOrderAndUpdateDiscountAutomatic(
                triggerType,
                this.customer,
                this.productsList
            )
            if (discList.isNotEmpty()) discOnePerOrderList.addAll(discList)
        }

        addRangeDiscountOnePerOrder(discOnePerOrderList)
    }

    fun addRangeDiscountOnePerOrder(discountOnePerOrderList: List<DiscountResp>) {
        discountOnePerOrderList.distinctBy { disc -> disc._id }.toList()
            .forEach { discOnePerOrder ->
                addDiscountOnePerOrder(discOnePerOrder)
            }
    }

    fun addDiscountOnePerOrder(discountOnePerOrder: DiscountResp) {
        val baseProductApplyDisc = getProductApplyOnePerOrderDisc(discountOnePerOrder)
        baseProductApplyDisc?.discountServersList?.add(discountOnePerOrder.clone())
    }

    fun getProductApplyOnePerOrderDisc(discOnePerOrder: DiscountResp): BaseProductInCart? {
        val applyToList = discOnePerOrder.Condition.CustomerBuys.ListApplyTo.toList()
        val productApplyList = getProductListApplyToDiscount(applyToList)
        if (productApplyList.isEmpty()) return null
        var applyValue = 0.0
        when (discOnePerOrder.ApplyToPriceProduct) {
            DiscountPriceType.HIGHEST.value -> {
                applyValue =
                    productApplyList.maxOfOrNull { basePro -> basePro.compareValue(applyToList) }
                        ?: 0.0
            }
            DiscountPriceType.LOWEST.value -> {
                applyValue =
                    productApplyList.minOfOrNull { basePro -> basePro.compareValue(applyToList) }
                        ?: 0.0
            }
        }
        return productApplyList.firstOrNull { basePro ->
            val subtotal = basePro.compareValue(applyToList)
            return@firstOrNull subtotal == applyValue
        }
    }

    fun getProductListApplyToDiscount(appliesTo: List<Product>): List<BaseProductInCart> {
        val baseProductList: MutableList<BaseProductInCart> = mutableListOf()
        appliesTo.forEach { productDisc ->
            val baseProduct = this.productsList.firstOrNull { p ->
                p.proOriginal?._id == productDisc._id
            }
            if (baseProduct != null) {
                baseProductList.add(baseProduct)
            }
        }
        return baseProductList
    }

    fun removeAllDiscountCoupon() {
        if (!anyProductList()) return
        this.discountServerList.removeAll { disc ->
            disc.isCoupon()
        }
        this.productsList.forEach { baseProduct ->
            baseProduct.removeAllDiscountCoupon()
        }
    }

    fun totalQtyDiscUsed(discountId: String): Int {
        val totalQty = discountServerList.count { disc ->
            disc._id == discountId
        };
        return totalQty ?: 0;
    }


}
