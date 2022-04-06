package com.hanheldpos.data.api.pojo.discount

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountUsed(
val DiscountCode: String ,

val DiscountGuid: String
) : Parcelable {
}