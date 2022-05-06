package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceReport(
    val DeviceGuid: String,
    val DeviceName: String,
    val Quantity: Int,
    val Total: Double
) : Parcelable