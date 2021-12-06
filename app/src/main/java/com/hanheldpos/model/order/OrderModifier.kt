package com.hanheldpos.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderModifier(
    val ModifierItemGuid :String,
    val Name : String,
    val Url: String? = null,
    val PriceOriginal : Double?,
    val Price : Double?,
    val ModifierQuantity : Int?,
    val ModifierSubtotal : Double?,
    val DiscountTotalPrice : Double?,
    val PricingMethodId: Int?,
    val DiscountValue: Double? = null
) : Parcelable
