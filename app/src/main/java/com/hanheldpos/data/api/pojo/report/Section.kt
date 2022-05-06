package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Section(
    val AverageGrossSale: Double,
    val AverageNetSale: Double,
    val Bill: Double,
    val Comp: Double,
    val Covers: Double,
    val Discount: Double,
    val FloorTable: List<FloorTableReport>,
    val GrossSales: Double,
    val NetSale: Double,
    val RefundAmount: Double,
    val SectionGuid: String,
    val SectionName: String,
    val Tips: Double
) : Parcelable