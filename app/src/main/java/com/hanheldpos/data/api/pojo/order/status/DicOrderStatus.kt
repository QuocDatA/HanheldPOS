package com.hanheldpos.data.api.pojo.order.status

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DicOrderStatus(
    val Active: Int,
    val Color: String,
    val FunctionPos: String,
    val FunctionWeb: String,
    val OrderStatusId: Int,
    val Title_en: String
) : Parcelable