package com.hanheldpos.model.cart

import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.OrderHelper
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
        val totalDiscServer  = discountServerList.sumOf { it.total(subTotal,0.0) ?: 0.0 }
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

    fun addDiscountServer(discount : DiscountResp) {
        discountServerList.add(discount)
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

        val discOnePerOrderList: List<DiscountResp> = listOf()
        this.productsList.forEach { baseProduct ->
            baseProduct.getOnePerOrderAndUpdateDiscountAutomatic(
                triggerType,
                this.customer,
                this.productsList
            )
        }

        addRangeDiscountOnePerOrder(discOnePerOrderList)
    }

    fun addRangeDiscountOnePerOrder(discountOnePerOrderList: List<DiscountResp>) {
        discountOnePerOrderList.distinctBy { disc -> disc._id }.toList()
            .forEach { discOnePerOrder -> addDiscountOnePerOrder(discOnePerOrder)
            }
    }

    fun addDiscountOnePerOrder(discountOnePerOrder: DiscountResp) {
        val baseProductApplyDisc = getProductApplyOnePerOrderDisc(discountOnePerOrder)
        baseProductApplyDisc.discountServersList?.add(discountOnePerOrder.clone())
    }

    fun getProductApplyOnePerOrderDisc(discOnePerOrder: DiscountResp): BaseProductInCart {
        var applyToList = discOnePerOrder.Condition.CustomerBuys.ListApplyTo.toList()
        var productApplyList = getProductListApplyToDiscount(applyToList)

        var applyValue = 0.0
        when (discOnePerOrder.ApplyToPriceProduct) {
            DiscountPriceType.HIGHEST.value -> {
                applyValue = productApplyList.maxOf { basePro -> basePro.compareValue(applyToList) }
            }
            DiscountPriceType.LOWEST.value -> {
                applyValue = productApplyList.minOf { basePro -> basePro.compareValue(applyToList) }
            }
        }
        return productApplyList.first {
                basePro -> val subtotal = basePro.compareValue(applyToList)
            return@first subtotal == applyValue
        }
    }

    fun getProductListApplyToDiscount(productList: List<Product>): List<BaseProductInCart> {
        var baseProductList: List<BaseProductInCart> = listOf()
        productList.forEach { productDisc ->
            val baseProduct = this.productsList.firstOrNull { p ->
                p.proOriginal?._id == productDisc._id
            }
            if (baseProduct != null) {
                baseProductList.toMutableList().add(baseProduct)
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

    fun totalQtyDiscUsed(discountId : String) : Int {
        val totalQty = discountServerList?.count {
            disc ->
            disc._id == discountId
        };
        return totalQty ?: 0;
    }
}
