package com.hanheldpos.data.api.pojo.order.menu

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Time(
    val PerLocation: String?,
    val TimeId: Int,
    val TimeOff: String,
    val TimeOn: String
) : Parcelable