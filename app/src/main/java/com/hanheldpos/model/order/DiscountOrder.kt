package com.hanheldpos.model.order

import android.os.Parcelable
import com.hanheldpos.model.discount.DiscountUser
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountOrder(
    var _id: String,
    var DiscountName: String,
    var DiscountCode: String? = null,
    var DiscountType: Int,
    var ParentTypeId: Int,
    var DiscountValue: Double,
    var DiscountTotalPrice: Double,
    var DiscountQuantity: Int,
    var DiscountLineTotalPrice: Double,
    var MaximumAmount: Double? = null,
) : Parcelable {
    constructor(
        src: DiscountUser,
        proSubtotal: Double,
        modSubtotal: Double,
        productOriginal_id: String,
        quantity: Int
    ) : this(_id = "",
        DiscountName = src.DiscountName,
        DiscountType = src.DiscountType,
        DiscountValue = src.DiscountValue,
        ParentTypeId = src.DiscountType,
        DiscountQuantity = quantity,
        DiscountTotalPrice = src.total(proSubtotal + modSubtotal),
        DiscountLineTotalPrice = src.total(proSubtotal + modSubtotal),)
}