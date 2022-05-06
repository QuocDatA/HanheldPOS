package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CashVoucherReport(
    val Title: String,
    val Quantity: Double?,
    val PaymentAmount: Double?
) : Parcelable
