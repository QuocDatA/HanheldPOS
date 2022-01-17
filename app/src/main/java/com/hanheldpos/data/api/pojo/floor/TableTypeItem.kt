package com.hanheldpos.data.api.pojo.floor

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TableTypeItem(

    @field:SerializedName("Name_en")
    val Name_en: String? = null,

    @field:SerializedName("Acronymn")
    val Acronymn: String? = null,

    @field:SerializedName("_rev")
    val _rev: String? = null,

    @field:SerializedName("Visible")
    val Visible: Int? = null,

    @field:SerializedName("TableTypeId")
    val TableTypeId: Int? = null,

    @field:SerializedName("Height")
    val Height: Int? = null,

    @field:SerializedName("OrderNo")
    val OrderNo: Int? = null,

    @field:SerializedName("_Id")
    val _Id: String? = null,

    @field:SerializedName("_key")
    val _key: Int? = null,

    @field:SerializedName("Name_vi")
    val Name_vi: String? = null,

    @field:SerializedName("Width")
    val Width: Int? = null,

    @field:SerializedName("Handle")
    val Handle: String? = null
) : Parcelable