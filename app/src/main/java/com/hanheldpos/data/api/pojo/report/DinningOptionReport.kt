package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DinningOptionReport(
    val DinningOptionId: Int,
    val DinningOptionName: String,
    val Quantity: Int,
    val Total: Double
) : Parcelable