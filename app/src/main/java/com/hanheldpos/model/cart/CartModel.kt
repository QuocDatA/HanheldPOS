package com.hanheldpos.model.cart

import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.data.api.pojo.order.settings.ListReasonsItem
import com.hanheldpos.data.api.pojo.table.FloorTableItem
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.fee.FeeApplyToType
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.product.BaseProductInCart

data class CartModel(
    var table: FloorTableItem,
    var customer: CustomerResp? = null,
    var customerQuantity: Int,
    var diningOption: DiningOptionItem,
    val fees: List<Fee>,
    var productsList: MutableList<BaseProductInCart>,
    var discountUserList: MutableList<DiscountUser>,
    var compReason : ListReasonsItem? = null,
) {

    fun getSubTotal() = productsList.sumOf {
        it.lineTotalValue
    }

    fun getDiscountPrice() = totalDiscount(getSubTotal());

    fun getTotalQuantity() = productsList.sumOf {
        it.quantity ?: 0
    }

    fun getTotalPrice() = total();

    private fun totalDiscount(subTotal: Double): Double {
        val totalDiscUser = discountUserList.sumOf { it.total(subTotal) };

        var total = totalDiscUser;
        return total;
    }

    fun totalFee(subTotal: Double, totalDiscount : Double) : Double {
        return fees.filter { FeeApplyToType.fromInt(it.feeApplyToType) != FeeApplyToType.Included }.sumOf { fee->
            fee.price(subTotal,totalDiscount)
        }
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

    fun addCompReason(reason : ListReasonsItem) {
        compReason = reason;
    }

    fun addDiscountUser(discount : DiscountUser){
        discountUserList = mutableListOf(discount);
    }

    fun clearCart() {
        productsList.clear();
        customer = null;
    }

}
