package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SalesSummary(
    val BillAverage: Double,
    val BillAverageGrossSale: Double,
    val BillAverageNetSale: Double,
    val BillCount: Double,
    val BillCountGrossSale: Double,
    val BillCountNetSale: Double,
    val Comp: Double,
    val Cover: Double,
    val GrossSale: Double,
    val NetSales: Double,
    val OrderAmount: Double,
    val OrderDiscount: Double,
    val OrderDiscountComp: Double,
    val OrderQuantity: Int,
    val OrderRefund: Double,
    val QuantityBillCount: Double,
    val QuantityRefund: Double,
    val ServiceCharge: Double,
    val ServiceFee: Double,
    val ShippingFee: Double,
    val SurchargeFee: Double,
    val TaxFee: Double,
    val TotalSales: Double
) : Parcelable