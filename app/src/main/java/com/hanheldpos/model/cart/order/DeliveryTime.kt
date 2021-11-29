package com.hanheldpos.model.cart.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeliveryTime(
    var DateText: String,
    var TimeTextval: String,
    var DateValue: String,
    var TimeValue: String,
    var IsNow: Boolean,
    var OrderWait: Int,
    var EstimateTime: Int,
) : Parcelable {
}