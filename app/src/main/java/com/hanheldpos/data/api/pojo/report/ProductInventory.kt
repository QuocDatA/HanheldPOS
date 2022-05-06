package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductInventory(
    val GrossQuantity: Int,
    val Name: String,
    val NetQuantity: Int,
    val ProductGuid: String,
    val RefundQuantity: Int
) : Parcelable