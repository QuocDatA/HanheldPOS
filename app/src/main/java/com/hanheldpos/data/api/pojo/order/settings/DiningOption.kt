package com.hanheldpos.data.api.pojo.order.settings

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiningOption(

    @field:SerializedName("Id")
    val Id: Int? = null,

    @field:SerializedName("Title")
    val Title: String? = null,

    @field:SerializedName("Acronymn")
    val Acronymn: String? = null,

    @field:SerializedName("OrderNo")
    val OrderNo: Int? = null,

    @field:SerializedName("TypeId")
    val TypeId: Int? = null,

    @field:SerializedName("TypeText")
    val TypeText: String? = null,

    @field:SerializedName("SubDiningOption")
    val SubDiningOption: List<SubDiningOptionItem>? = null,

    ) : Parcelable
