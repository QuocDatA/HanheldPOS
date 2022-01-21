package com.hanheldpos.data.api.pojo.fee

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountsApplyToItem(
    val ApplyTo: Int,
    val DiscountAutomatic: Boolean,
    val DiscountGuid: String,
    val DiscountType: Int,
    val ListCombo: String?,
    val ListParents: String,
    val ListProducts: String,
    val UserGuid: String
) : Parcelable