package com.hanheldpos.data.api.pojo.order.settings

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubDiningOptionItem(

    @field:SerializedName("SubId")
    val SubId: Int? = null,

    @field:SerializedName("SubTitle")
    val SubTitle: String? = null,

    @field:SerializedName("Location")
    val Location: String? = null,
) : Parcelable
