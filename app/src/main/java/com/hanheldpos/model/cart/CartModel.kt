package com.hanheldpos.model.cart

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.data.api.pojo.table.FloorTableItem
import com.hanheldpos.model.DataHelper.getDefaultDiningOptionItem
import com.hanheldpos.model.cart.order.OrderItemModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartModel(
    var orderGuid: String? = null,
    //table info
    var table: FloorTableItem? = null,

    // Number of Customer
    var customerQuantity : Int = 0,

    //order item list of Order
    var listOrderItem : MutableList<OrderItemModel> = mutableListOf(),

    //dining option of Order
    var diningOption: DiningOptionItem? = getDefaultDiningOptionItem(),


) : Parcelable {
    fun getQuantityCart() : Int {
        return listOrderItem.sumOf {
            it.getOrderQuantity()
        }
    }


    fun getSubTotal() = listOrderItem.sumOf {
        it.getOrderPrice()
    }

    fun getTotalDisc() : Double {
        return 0.0;
    }

    fun getPrice() : Double{
        val subtotal = getSubTotal();
        val totalDisc = getTotalDisc();
        var subIncDisc = subtotal - totalDisc;
        subIncDisc = if (subIncDisc < 0 ) 0.0 else subIncDisc;

        //TODO : use FeeApplyToType to calculate price

        return subIncDisc;
    }
}
