package com.hanheldpos.model.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductComboItem(
    @field:SerializedName("AppliesTo")
    val AppliesTo: Int? = null,

    @field:SerializedName("Quantity")
    val Quantity: Int? = null,

    @field:SerializedName("Id")
    val Id: Int? = null,

    @field:SerializedName("ComboGuid")
    val ComboGuid: String? = null
) : Parcelable