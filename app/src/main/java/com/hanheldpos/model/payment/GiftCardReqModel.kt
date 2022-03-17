package com.hanheldpos.model.payment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class GiftCardReqModel(
    val ApplyToId: Double?,
    val ApplyToPayment: Double,
    val CardCode: String,
    val CustomerGuid: String,
    val DeviceGuid: String,
    val EmployeeGuid: String,
    val LocationGuid: String,
    val MethodId: String,
    val OrderGuid: String,
    val Payable: Double?,
    val Receivable: Double?,
    val UserGuid: String
) : Parcelable