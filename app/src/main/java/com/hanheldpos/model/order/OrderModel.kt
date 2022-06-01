package com.hanheldpos.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderModel(
    val Order : Order,
    val OrderDetail : OrderDetail,
    val OrderSummary: OrderSummaryPrimary,
) : Parcelable {

}
