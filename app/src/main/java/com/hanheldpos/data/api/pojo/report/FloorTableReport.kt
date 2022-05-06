package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FloorTableReport(
    val AverageGrossSale: Double,
    val AverageNetSale: Double,
    val Bill: Double,
    val Covers: Double,
    val GrossSales: Double,
    val Name: String,
    val NetSale: Double,
    val Tips: Double
) : Parcelable