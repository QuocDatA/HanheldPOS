package com.hanheldpos.data.api.pojo.order.status

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListDinningOptions(
    val Id: Int,
    val ListOrderSettingStatus: List<OrderSettingStatus>,
    val Title: String,
    val Visible: Int
) : Parcelable