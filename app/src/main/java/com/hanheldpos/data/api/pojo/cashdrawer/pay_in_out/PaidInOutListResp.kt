package com.hanheldpos.data.api.pojo.cashdrawer.pay_in_out

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class PaidInOutListResp(
    val _id: String,
    val _rev: String,
    val _key: Long,
    val CashDrawerGuid: String,
    val Payable: Double?,
    val Receivable: Double?,
    val Description: String,
    val Code: String,
    val CreateDate: String,
    val LocationGuid: String,
    val UserGuid: String,
    val DeviceGuid: String,
    val EmployeeGuid: String,
) : Parcelable