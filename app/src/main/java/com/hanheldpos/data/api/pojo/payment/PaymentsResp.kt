package com.hanheldpos.data.api.pojo.payment

import android.os.Parcelable


@kotlinx.parcelize.Parcelize
data class PaymentsResp(
    val DidError: Boolean,
    val ErrorMessage: String,
    val Message: String,
    val Model: List<PaymentMethodResp>
) : Parcelable

