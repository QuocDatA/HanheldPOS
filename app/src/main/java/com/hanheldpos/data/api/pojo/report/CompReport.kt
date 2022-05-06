package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompReport(
    val Name: String,
    val Quantity: Long?,
    val Amount: Double?
) : Parcelable
