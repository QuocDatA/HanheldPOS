package com.hanheldpos.data.api.pojo.payment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListPaymentItem(
    val Money: Double,
    val Title: String
) : Parcelable