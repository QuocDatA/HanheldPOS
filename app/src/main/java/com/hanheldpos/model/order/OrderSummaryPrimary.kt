package com.hanheldpos.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderSummaryPrimary(
    val OrderCode: String? = null,
    val OrderStatusId: Int,
    val PaymentStatusId: Int,
    val Description: String,
    val GrandTotal: Double?,
    val TableId: String,
    val TableName : String,
    val DiningOptionId: Int?,
    val DiningOptionName: String?,
    val CreateDate: String,
    val Synced: Boolean? = null,
) : Parcelable {
}