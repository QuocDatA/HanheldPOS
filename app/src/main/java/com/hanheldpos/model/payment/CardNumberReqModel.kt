package com.hanheldpos.model.payment

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.payment.Voucher
import kotlinx.parcelize.Parcelize


@Parcelize
data class CardNumberReqModel(
    val ApplyToId: Int?,
    val ApplyToPayment: List<Voucher>? = null,
    val CardCode: String,
    val CustomerGuid: String?,
    val DeviceGuid: String,
    val EmployeeGuid: String,
    val LocationGuid: String,
    val MethodId: String,
    val OrderGuid: String,
    val Payable: Double?,
    val Receivable: Double? = null,
    val UserGuid: String
) : Parcelable