package com.hanheldpos.model.cashdrawer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CashDrawerStatusReq(
    val UserGuid: String,
    val LocationGuid: String,
    val DeviceGuid: String,
    val EmployeeGuid: String,
) : Parcelable
