package com.hanheldpos.data.api.pojo.order.settings

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderSettingResp(

    @field:SerializedName("ListVoid")
    val ListVoid: List<ListVoidItem>? = null,

    @field:SerializedName("ListDiningOptions")
    val ListDiningOptions: List<DiningOption>? = null,

    @field:SerializedName("ListComp")
    val ListComp: List<ListCompItem>? = null

) : Parcelable

