package com.hanheldpos.model.cart.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BuyXGetYEntire(
    var MinimumTypeId: Int,
    var MinimumValue: Double,
    var RemainingMinimumValue: Double,
    var ApplyTo: Int,
    var EntrieOderPercentValue: Double,
    var DiscountValue: Double,
    var DiscountValueType: Int,
    var IsCompleted: Boolean
) : Parcelable {
}