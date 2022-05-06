package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderPayment(
    val PaymentAmount: Double,
    val PaymentMethod: String,
    val PaymentMethodId: Int,
    val PercentPaymentAmount: Double,
    val PercentQuantity: Double,
    val Quantity: Int,
    val QuantityRefund: Int,
    val RefundAmount: Double,
    val _id: String
) : Parcelable