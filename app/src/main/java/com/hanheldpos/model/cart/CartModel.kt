package com.hanheldpos.model.cart

import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.fee.FeeApplyToType
import com.hanheldpos.model.cart.payment.PaymentOrder
import com.hanheldpos.model.discount.DiscountServer
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.home.table.TableSummary
import com.hanheldpos.model.order.DeliveryTime
import com.hanheldpos.model.order.Order
import com.hanheldpos.model.order.Shipping
import com.hanheldpos.model.product.BaseProductInCart

data class CartModel(
    var order : Order? = null,
    var table: TableSummary,
    var customer: CustomerResp? = null,
    var shipping: Shipping? = null,
    var diningOption: DiningOptionItem,
    var deliveryTime: DeliveryTime? = null,
    val fees: List<Fee>,
    var paymentsList : MutableList<PaymentOrder>,
    var productsList: MutableList<BaseProductInCart>,
    var discountUserList: MutableList<DiscountUser>,
    var discountServerList : MutableList<DiscountServer>,
    var compReason : Reason? = null,
    var createDate : String? = null,
    var orderCode : String? = null,
    var menuLocationGuid : String?=null,
) {

    fun getSubTotal() = productsList.sumOf {
        it.lineTotalValue
    }

    fun getDiscountPrice() = totalDiscount(getSubTotal());

    fun getTotalQuantity() = productsList.sumOf {
        it.quantity ?: 0
    }

    fun getTotalPrice() = total();

    fun updatePriceList(menuLocation_id : String){
        this.menuLocationGuid = menuLocation_id;
    }

    fun totalDiscount(subTotal: Double): Double {
        val totalDiscUser = discountUserList.sumOf { it.total(subTotal) };

        var total = totalDiscUser;
        return total;
    }

    fun totalFee(subTotal: Double, totalDiscount : Double) : Double {
        return fees.filter { FeeApplyToType.fromInt(it.feeApplyToType) != FeeApplyToType.Included }.sumOf { fee->
            fee.price(subTotal,totalDiscount)
        }
    }

    fun totalGross(subTotal: Double, totalDiscount : Double) : Double {
        return subTotal + totalDiscount;
    }

    fun totalTemp() : Double {
        val totalDiscPrice = totalDiscount(getSubTotal());
        val totalFeePrice = totalFee(getSubTotal(),totalDiscPrice);
        var total = getSubTotal() + totalFeePrice - totalDiscPrice;
        total = if (total < 0) 0.0 else total;
        return total;
    }

    fun totalComp(totalTemp : Double) : Double {
        val comp = compReason?.total(totalTemp);
        return comp?: 0.0;
    }

    fun totalComp() : Double {
        val totalTemp = totalTemp();
        val comp = compReason?.total(totalTemp);
        return comp?: 0.0;
    }

    fun total() : Double {
        val totalTemp = totalTemp();
        val totalComp = totalComp(totalTemp);

        val lineTotal = totalTemp - totalComp;
        return lineTotal
    }

    fun addRegular(regular: Regular){
        val listFee = DataHelper.findFeeProductList(regular.proOriginal!!.id);
        regular.fees = listFee;
        productsList.add(regular);
    }

    fun addBundle(combo : Combo){
        val listFee = DataHelper.findFeeProductList(combo.proOriginal!!.id);
        combo.fees = listFee;
        productsList.add(combo);
    }

    fun addCompReason(reason : Reason) {
        compReason = reason;
    }

    fun addDiscountUser(discount : DiscountUser){
        discountUserList = mutableListOf(discount);
    }

    fun addPayment(payment : PaymentOrder) {
        paymentsList = mutableListOf(payment);
    }

    fun clearCart() {
        productsList.clear();
        customer = null;
    }

}
