package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cash(
    val ActualInDrawer: Double,
    val CashRefunds: Double,
    val CashSales: Double,
//    val Closer: Any,
    val Code: String,
    val CreateDate: String,
    val DateClosed: String,
    val DateIn: String,
//    val DateOut: Any,
    val DeviceGuid: String,
    val Difference: Double,
    val DrawerDescription: String,
    val EmployeeGuid: String,
    val EndCash: Double,
//    val Ender: Any,
    val ExpectedInDrawer: Double,
    val LocationGuid: String,
    val PaidIn: Double,
    val PaidOut: Double,
    val Starter: String,
    val StartingCash: Double,
    val StatusId: Int,
    val TypeCloseId: Int,
    val TypeEndId: Int,
    val TypeStartId: Int,
    val UserGuid: String,
    val _id: String,
    val _key: Int,
    val _rev: String
) : Parcelable