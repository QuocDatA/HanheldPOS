package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListComboReport(
    val ProductGuid: String,
    val Name: String,
    val Qty: Long,
    val RefundQty: Long,
    val NetQty: Long
) : Parcelable
