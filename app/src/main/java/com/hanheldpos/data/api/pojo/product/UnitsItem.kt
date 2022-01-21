package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UnitsItem(

    @field:SerializedName("SystemUnitGroupId")
    val SystemUnitGroupId: Int? = null,

    @field:SerializedName("Abbreviation")
    val Abbreviation: String? = null,

    @field:SerializedName("SystemPrecisionName")
    val SystemPrecisionName: String? = null,

    @field:SerializedName("SystemUnitId")
    val SystemUnitId: Int? = null,

    @field:SerializedName("UserGuid")
    val UserGuid: String? = null,

    @field:SerializedName("_rev")
    val _rev: String? = null,

    @field:SerializedName("Visible")
    val Visible: Int? = null,

    @field:SerializedName("SystemUnitName")
    val SystemUnitName: String? = null,

    @field:SerializedName("_Id")
    val _Id: String? = null,

    @field:SerializedName("_key")
    val _key: Int? = null
) : Parcelable
