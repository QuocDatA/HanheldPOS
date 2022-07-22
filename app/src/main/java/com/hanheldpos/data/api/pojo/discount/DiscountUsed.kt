package com.hanheldpos.data.api.pojo.discount

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountUsed(
    @SerializedName("_id")
    val Id: String? = null,

    val DiscountName: String? = null,

    val DiscountCode: String,

    val DiscountQuantity: Int? = null,
) : Parcelable