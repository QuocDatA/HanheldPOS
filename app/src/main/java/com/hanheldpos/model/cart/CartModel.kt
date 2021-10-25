package com.hanheldpos.model.cart

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.order.settings.DiningOptionItem
import com.hanheldpos.data.api.pojo.table.FloorTableItem
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

    //order item list of Order
    var listOrderItem: MutableList<OrderItemModel> = mutableListOf(),

    //dining option of Order
    var diningOption: DiningOptionItem? = getDefaultDiningOptionItem(),


    ) : Parcelable {
    fun getQuantityCart(): Int {
        return listOrderItem.sumOf {
            it.getOrderQuantity()
        }
    }


    fun getSubTotal() = listOrderItem.sumOf {
        it.getOrderPrice()
    }

    fun getTotalDisc(): Double {
        return 0.0;
    }

    @JvmOverloads
    fun getPrice(subtotal: Double = getSubTotal(), totalDisc: Double = getTotalDisc()): Double {

        var subIncDisc = subtotal - totalDisc

        subIncDisc = if (subIncDisc < 0) 0.0 else subIncDisc


        /// TODO dealing with missing Id and Value as suggested since these fields does not available in cart
        val Id: FeeApplyToType = FeeApplyToType.Included
        val Value: Double = 0.0

        return when (Id) {
            FeeApplyToType.NotIncluded -> subIncDisc * (Value / 100)
            FeeApplyToType.Included -> subIncDisc - (subIncDisc / ((Value + 100) / 100))
            FeeApplyToType.Order -> subIncDisc * (Value / 100)
        }


//        val subtotal = getSubTotal();
//        val totalDisc = getTotalDisc();
//        var subIncDisc = subtotal - totalDisc;
//        subIncDisc = if (subIncDisc < 0) 0.0 else subIncDisc;
//
//        //TODO : use FeeApplyToType to calculate price
//
//        return subIncDisc;
    }
}
