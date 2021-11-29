package com.hanheldpos.data.api.pojo.payment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentMethodResp(
    val ApplyToId: Int,
    val Color: String,
    val Css: String,
    val Icon: String,
    val ListPayment: List<Voucher>,
    val MethodId: Int,
    val OrderNo: Int,
    val PaymentMethodType: Int,
    val Sub: String,
    val Title: String,
    val _id: String
) : Parcelable