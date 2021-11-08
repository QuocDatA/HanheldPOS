package com.hanheldpos.model.cart

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.data.api.pojo.table.FloorTableItem
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.DataHelper.getDefaultDiningOptionItem
import com.hanheldpos.model.cart.fee.FeeApplyToType
import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.home.table.TableStatusType
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartModel(
    var orderCode: String? = null,
    //table info
    var table: FloorTableItem? = null,

    // Number of Customer
    var customerQuantity: Int = 0,

    // customer
    var customer : CustomerResp?= null,

    //order item list of Order
    var listOrderItem: MutableList<OrderItemModel> = mutableListOf(),

    //dining option of Order
    var diningOption: DiningOptionItem? = getDefaultDiningOptionItem(),

    val feeType : FeeApplyToType? = null,


    ) : Parcelable {
    fun getQuantityCart(): Int {
        return listOrderItem.sumOf {
            it.getOrderQuantity()
        }
    }


    fun getSubTotal() = listOrderItem.sumOf {
        it.getPriceLineTotal()
    }

    fun getTotalDisc(): Double {
        return 0.0;
    }

    @JvmOverloads
    fun getFee(subtotal: Double = getSubTotal(), totalDisc: Double = getTotalDisc()): Double {
        var subIncDisc = subtotal - totalDisc

        subIncDisc = if (subIncDisc < 0) 0.0 else subIncDisc

        val valueFee = DataHelper.getValueFee(feeType);
        return when (feeType) {
            FeeApplyToType.Order -> subIncDisc * (valueFee / 100);
            else->0.0
        }
    }

    fun getLineTotal() : Double{
        return getSubTotal() - getTotalDisc() + getFee();
    }

    fun clearCart() {
        listOrderItem.clear();
        customer = null;
    }

}
