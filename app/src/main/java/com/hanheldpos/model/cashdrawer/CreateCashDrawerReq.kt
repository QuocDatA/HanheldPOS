package com.hanheldpos.model.cashdrawer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CreateCashDrawerReq(
    val UserGuid: String,
    val LocationGuid: String,
    val DeviceGuid: String,
    val EmployeeGuid: String,
    val CashDrawerType: Int,
    val StartingCash: Double? = 0.0,
    val ActualInDrawer: Double?,
    val DrawerDescription: String,
) : Parcelable {
}