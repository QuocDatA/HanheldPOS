package com.hanheldpos.data.api.pojo.order.status

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderStatusResp(
    val Fullfillments: Fullfillments,
    val Payments: PaymentsOrderStatus
) : Parcelable