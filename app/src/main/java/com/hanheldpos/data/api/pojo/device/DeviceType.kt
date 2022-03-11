package com.hanheldpos.data.api.pojo.device

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceType(
    val Id: Int,
    val Title: String
) : Parcelable