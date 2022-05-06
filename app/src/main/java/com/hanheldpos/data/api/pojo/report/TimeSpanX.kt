package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimeSpanX(
    val Comp: Double,
    val Discount: Double,
    val End: String,
    val NetSales: Double,
    val Percent: Double,
    val Quantity: Int,
    val QuantityBillCount: Int,
    val QuantityRefund: Int,
    val RefundAmount: Double,
    val Start: String,
    val Total: Double,
    val TotalPrice: Double
) : Parcelable