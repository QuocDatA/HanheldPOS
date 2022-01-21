package com.hanheldpos.model.order

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.fee.Fee
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderFee(
    var _id: String,
    var FeeType: Int,
    var FeeName: String,
    var FeeValue: Double,
    var TotalPrice: Double
) : Parcelable {
    constructor(src: Fee, subtotal: Double, totalDiscounts: Double) : this(
        _id = src._id,
        FeeType = src.Id,
        FeeName = src.Name,
        FeeValue = src.Value,
        TotalPrice = src.price(subtotal, totalDiscounts)
    )
}