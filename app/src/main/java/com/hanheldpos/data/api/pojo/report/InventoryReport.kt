package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InventoryReport(
    val CategoryGuid: String,
    val CategoryName: String,
    val NetQty: Int,
    val Product: List<ProductInventory>,
    val Qty: Int,
    val RefundQty: Int
) : Parcelable