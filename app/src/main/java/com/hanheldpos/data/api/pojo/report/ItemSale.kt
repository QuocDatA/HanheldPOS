package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemSale(
    val Comp: Double,
    val ExtraTotalPrice: Double,
    val GrossPrice: Double,
    val LineDiscount: Double,
    val LineService: Double,
    val LineShipping: Double,
    val LineSurcharge: Double,
    val LineTax: Double,
    val LineTotal: Double,
    val NetQty: Int,
    val NetSales: Double,
    val Price: Double,
    val ProductGuid: String,
    val ProductName: String,
    val Quantity: Int,
    val Return: Double,
    val SKU: String,
    val SubTotal: Double,
    val TotalSales: Double,
    val Variants: String
) : Parcelable