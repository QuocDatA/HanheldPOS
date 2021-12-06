package com.hanheldpos.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderSubmitResp(
    val Code: String,
    val Message: String,
    val _id: String
) : Parcelable