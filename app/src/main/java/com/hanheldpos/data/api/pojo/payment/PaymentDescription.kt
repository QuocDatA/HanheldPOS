package com.hanheldpos.data.api.pojo.payment

import android.os.Parcelable


@kotlinx.parcelize.Parcelize
data class PaymentDescription(
    val ApplyToId: ApplyToIdPaymentResp,
    val PaymentMethodType: PaymentMethodTypeResp
) : Parcelable

@kotlinx.parcelize.Parcelize
data class ApplyToIdPaymentResp(
    val `1`: String,
    val `10`: String,
    val `11`: String,
    val `2`: String,
    val `3`: String,
    val `30`: String,
    val `4`: String,
    val `5`: String,
    val `6`: String,
    val `7`: String,
    val `8`: String,
    val `9`: String
) : Parcelable

@kotlinx.parcelize.Parcelize
data class PaymentMethodTypeResp(
    val `1`: String,
    val `10`: String,
    val `2`: String,
    val `3`: String,
    val `4`: String,
    val `5`: String
) : Parcelable