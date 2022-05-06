package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllCategory(
    val CateGuid: String,
    val CateName: String,
    val GrossPrice: Double,
    val LineDiscount: Double,
    val LineService: Double,
    val LineShipping: Double,
    val LineSurcharge: Double,
    val LineTax: Double,
    val PercentQuantityCategory: Double,
    val PercentTotalCategory: Double,
    val Product: List<ProductReport>,
    val Quantity: Int,
    val ReferenceId: String,
    val SubTotal: Double
) : Parcelable