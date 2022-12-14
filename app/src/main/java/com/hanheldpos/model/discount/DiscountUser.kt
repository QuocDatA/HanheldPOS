package com.hanheldpos.model.discount

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountUser(
    val DiscountGuid: String? = null,
    val DiscountName: String,
    val DiscountType: Int,
    val DiscountValue : Double,
) : Parcelable {
    fun total(subtotal : Double) : Double {
        when (DiscountTypeEnum.fromInt(DiscountType)) {
            DiscountTypeEnum.AMOUNT->{
                return DiscountValue
            }
            DiscountTypeEnum.PERCENT->{
                return subtotal * DiscountValue / 100
            }
            else -> {}
        }
        return 0.0
    }
}
