package com.hanheldpos.model.cart

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VariantCart(
    val Id: Int,
    val Value: String,
) : Parcelable