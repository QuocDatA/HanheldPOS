package com.hanheldpos.data.api.pojo.order.settings

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reason(

    @field:SerializedName("Visible")
    val Visible: Int? = null,

    @field:SerializedName("Title")
    val Title: String? = null,

    @field:SerializedName("Id")
    val Id: Int? = null,

    @field:SerializedName("CompVoidGuid")
    val CompVoidGuid: String,

    @field:SerializedName("CompVoidValue")
    val CompVoidValue: Int,
) : Parcelable {
    fun total(total : Double) : Double {
        val totalComp = total * CompVoidValue / 100
        return totalComp
    }
}