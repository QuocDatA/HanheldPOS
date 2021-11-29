package com.hanheldpos.model.cart.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompVoid(
    var CompVoidGuid: String,
    var CompVoidGroupId: Int,
    var CompVoidName: String,
    var CompVoidValue: Int,
    var CompVoidTotalPrice: Double
) : Parcelable {
}