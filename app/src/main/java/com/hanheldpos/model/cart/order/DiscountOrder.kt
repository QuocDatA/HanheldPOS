package com.hanheldpos.model.cart.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountOrder(
    var _id: String,
    var DiscountName: String,
    var DiscountCode: String,
    var DiscountType: Int,
    var ParentTypeId: Int,
    var DiscountValue: Double,
    var DiscountTotalPrice: Double,
    var DiscountQuantity: Int,
    var DiscountLineTotalPrice: Double,
    var MaximumAmount: Double,
) : Parcelable {
}