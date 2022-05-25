package com.hanheldpos.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BuyXGetYEntire(
    var ApplyTo: Int,
    var MinimumTypeId: Int,
    var MinimumValue: Double,
    var RemainingMinimumValue: Double,
    var EntrieOderPercentValue: Double,
    var DiscountValue: Double,
    var DiscountValueType: Int,
    var IsCompleted: Boolean
) : Parcelable {
    constructor(
        ApplyTo: Int,
        MinimumTypeId: Int,
        MinimumValue: Double,
        DiscountValue: Double,
        DiscountValueType: Int,
    ) : this(
        ApplyTo = ApplyTo,
        MinimumTypeId = MinimumTypeId,
        MinimumValue = MinimumValue,
        RemainingMinimumValue = 0.0,
        DiscountValue = DiscountValue,
        DiscountValueType = DiscountValueType,
        EntrieOderPercentValue = 100.0,
        IsCompleted = true
    )
}