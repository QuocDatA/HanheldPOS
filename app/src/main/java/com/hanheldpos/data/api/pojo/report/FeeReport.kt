package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeeReport(
    val _id: String,
    val FeeName: String,
    val Quantity: Long?,
    val Amount: Double?
) : Parcelable
