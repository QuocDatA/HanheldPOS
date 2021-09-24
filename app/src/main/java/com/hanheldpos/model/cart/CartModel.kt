package com.hanheldpos.model.cart

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.order.settings.ListDiningOptionsItem
import com.hanheldpos.data.api.pojo.table.FloorTableItem
import com.hanheldpos.model.DataHelper.getDefaultDiningOptionItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartModel(
    var orderGuid: String? = null,
    //table info
    var table: FloorTableItem? = null,

    // Number of Customer
    var customerQuantity : Int = 0,

    //order item list of Order


    //dining option of Order
    var diningOption: ListDiningOptionsItem? = getDefaultDiningOptionItem(),
) : Parcelable {}