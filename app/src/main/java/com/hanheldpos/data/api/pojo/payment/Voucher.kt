package com.hanheldpos.data.api.pojo.payment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Voucher(
    val Money: Double,
    val Title: String
) : Parcelable {
}