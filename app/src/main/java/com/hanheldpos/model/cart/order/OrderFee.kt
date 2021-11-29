package com.hanheldpos.model.cart.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderFee(
    var _id: String,
    var FeeType: Int,
    var FeeName: String,
    var FeeValue: Double,
    var TotalPrice: Double
) : Parcelable {
}