package com.hanheldpos.model.cart.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderSummary(
    var Subtotal: Double,
    var Grandtotal: Double,
    var Points: Double,
    var GrossPrice: Double,
    var Discount: Double,
    var Service: Double,
    var Surcharge: Double,
    val Tax: Double,
    val Balance: Double,
    val Received: Double,
    val Change: Double,
    val PaymentAmount: Double,
    var Note: String,
    val OtherFee: Double,
) : Parcelable {
}