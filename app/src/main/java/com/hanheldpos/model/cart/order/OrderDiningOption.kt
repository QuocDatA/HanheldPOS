package com.hanheldpos.model.cart.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDiningOption(
    var Id: Int,
    var Title: String,
    var TypeId: Int,
    var Acronymn: String,
) : Parcelable {
}