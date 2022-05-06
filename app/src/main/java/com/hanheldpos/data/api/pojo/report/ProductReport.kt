package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductReport(
    val GrossPrice: Double,
    val LineDiscount: Double,
    val LineService: Double,
    val LineShipping: Double,
    val LineSurcharge: Double,
    val LineTax: Double,
    val LineTotal: Double,
    val PercentLineTotal: Double,
    val PercentQuantity: Double,
    val Price: Double,
    val ProductGuid: String,
    val ProductName: String,
    val Quantity: Int,
    val SKU: String,
    val SubTotal: Double,
    val Variants: String,
    val OrderGuid: String,
    val CreateDate : String,
    val DeliveryTypeId : Long,
    val DeviceGuid : String,
) : Parcelable