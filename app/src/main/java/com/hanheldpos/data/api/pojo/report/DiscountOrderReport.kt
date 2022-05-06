package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountOrderReport(
    val DiscountName: String,
    val DiscountTypeName: String,
    val Quantity: Long,
    val DiscountAmount: Double?
) : Parcelable