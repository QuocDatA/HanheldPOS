package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryReport(
    val AllCategory: List<AllCategory>,
    val Discount: Double,
    val GrossTotal: Double,
    val Quantity: Double,
    val Service: Double,
    val Shipping: Double,
    val SubTotal: Double,
    val Surcharge: Double,
    val Tax: Double,
    val Total: Double
) : Parcelable