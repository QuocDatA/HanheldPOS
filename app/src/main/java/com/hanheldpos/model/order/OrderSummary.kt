package com.hanheldpos.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderSummary(
    var Subtotal: Double,
    var Grandtotal: Double,
    var Points: Double? = 0.0,
    var GrossPrice: Double,
    var Discount: Double,
    var Service: Double?,
    var Surcharge: Double?,
    val Tax: Double?,
    val Balance: Double,
    val Received: Double,
    val Change: Double,
    val PaymentAmount: Double,
    var Note: String? = null,
    val OtherFee: Double,
) : Parcelable {
}