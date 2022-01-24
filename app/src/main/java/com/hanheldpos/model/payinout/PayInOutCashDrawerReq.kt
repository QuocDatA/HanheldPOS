package com.hanheldpos.model.payinout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PayInOutCashDrawerReq(
    val Description: String,
    val Receivable: Double?,
    val Payable: Double?,
    val Source: Int,
    val CashDrawerGuid: String,
    val UserGuid: String,
    val LocationGuid: String,
    val DeviceGuid: String,
    val EmployeeGuid: String,
) : Parcelable
