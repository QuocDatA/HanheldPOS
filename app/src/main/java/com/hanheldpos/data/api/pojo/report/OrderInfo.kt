package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderInfo(
    val CustomerNumber: Int,
    val OrderDiscount: Double,
    val OrderGrandTotal: Double,
    val OrderNumber: Int,
    val OrderServiceFee: Double,
    val OrderShipping: Double,
    val OrderSubTotal: Double,
    val OrderSurcharge: Double,
    val OrderTaxes: Double,
    val OrderTipFee: Double?,
    val OrderTipFree: Double
) : Parcelable