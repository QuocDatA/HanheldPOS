package com.hanheldpos.model.order

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.model.discount.DiscountTypeEnum
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
        src: DiscountResp,
        proSubtotal: Double?,
        modSubtotal: Double?,
        productOriginal_id: String?,
        quantity: Int
    ) : this(
        _id = src._id,
        DiscountName = src.DiscountName,
        DiscountCode = if (src.DiscountAutomatic) "" else src.DiscountCode,
        DiscountType = if (src.DiscountType == DiscountTypeEnum.BUYX_GETY.value) src.Condition.CustomerGets.DiscountValueType else src.DiscountType,
        DiscountValue = if (src.DiscountType == DiscountTypeEnum.BUYX_GETY.value) src.Condition.CustomerGets.DiscountValue else src.Condition.DiscountValue,
        ParentTypeId = src.DiscountType,
        DiscountQuantity = src.getQuantityUsed,
        DiscountTotalPrice = (src.total(proSubtotal, modSubtotal, productOriginal_id, quantity)
            ?: 0.0).div(src.getQuantityUsed),
        DiscountLineTotalPrice = src.total(proSubtotal, modSubtotal, productOriginal_id, quantity)
            ?: 0.0,
        MaximumAmount = src.getAmountUsed(productOriginal_id)
    )

    constructor(
        src: DiscountUser,
        proSubtotal: Double,
        modSubtotal: Double,
        productOriginal_id: String?,
        quantity: Int
    ) : this(
        _id = "",
        DiscountName = src.DiscountName,
        DiscountType = src.DiscountType,
        DiscountValue = src.DiscountValue,
        ParentTypeId = src.DiscountType,
        DiscountQuantity = 1,
        DiscountTotalPrice = src.total(proSubtotal + modSubtotal),
        DiscountLineTotalPrice = src.total(proSubtotal + modSubtotal),
    )
}