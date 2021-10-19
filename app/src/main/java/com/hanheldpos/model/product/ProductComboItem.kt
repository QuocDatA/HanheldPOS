package com.hanheldpos.model.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductComboItem(

    @field:SerializedName("AppliesTo")
    val appliesTo: Int? = null,

    @field:SerializedName("Quantity")
    val quantity: Int? = null,

    @field:SerializedName("Id")
    val id: Int? = null,

    @field:SerializedName("ComboGuid")
    val comboGuid: String? = null
) : Parcelable