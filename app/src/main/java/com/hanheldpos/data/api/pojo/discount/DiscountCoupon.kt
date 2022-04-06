package com.hanheldpos.data.api.pojo.discount

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountCoupon(
    val ProductGuid: String,

    val Variants: String,

    val OrderDetailId: Int?,

    val OrderDetailType: Int?,

    val DiscountGuid: String,

    val DiscountName: String,

    val DiscountCode: String,

    val DiscountType: Int?,

    val DiscountValue: Double?,

    val DiscountTotalPrice: Double?,

    val Quantity: Int?,

    val DiscountLineTotalPrice: Double?
) : Parcelable {
}