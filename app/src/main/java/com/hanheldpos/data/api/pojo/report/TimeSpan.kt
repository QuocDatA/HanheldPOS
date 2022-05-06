package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimeSpan(
    val Quantity: Int,
    val TimeSpan: List<TimeSpanX>,
    val Total: Double
) : Parcelable