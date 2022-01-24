package com.hanheldpos.model.payinout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaidInOutListCashDrawerReq(
    val UserGuid: String?,
    val LocationGuid: String?,
    val DeviceGuid: String?,
    val EmployeeGuid: String?,
    val CashDrawerGuid: String?,
) : Parcelable
