package com.hanheldpos.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderSubmitResp(
    val Code: String,
    val OrderDetail_id: String?,
    val _id: String
) : Parcelable