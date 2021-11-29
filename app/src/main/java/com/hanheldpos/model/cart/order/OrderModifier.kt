package com.hanheldpos.model.cart.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderModifier(
    val ModifierItemGuid :String,
    val Name : String,
    val Url: String,
    val PriceOriginal : Double?,
    val Price : Double?,
    val ModifierQuantity : Int?,
    val ModifierSubtotal : Double?,
    val DiscountTotalPrice : Double?,
    val PricingMethodId: Int?,
    val DiscountValue: Double?
) : Parcelable
