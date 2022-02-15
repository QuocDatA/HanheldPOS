package com.hanheldpos.data.api.pojo.order.settings

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubDiningOptionItem(

    @field:SerializedName("SubId")
    val SubId: Int? = null,

    @field:SerializedName("NickName")
    val NickName: String? = null,

    @field:SerializedName("LocationGuid")
    val LocationGuid: String? = null,
) : Parcelable
