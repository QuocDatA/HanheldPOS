package com.hanheldpos.data.api.pojo.order.status

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderSettingStatus(
    val DinningOptionId: Int,
    val DinningOptionVisible: Int,
    val ListDicOrderStatus: List<DicOrderStatus>,
    val OrderStatusGroupId: Int
) : Parcelable