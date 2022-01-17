package com.hanheldpos.data.api.pojo.order.settings

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListVoidItem(

    @field:SerializedName("GroupName")
    val GroupName: String? = null,

    @field:SerializedName("Id")
    val Id: Int? = null,

    @field:SerializedName("ListReasons")
    val ListReasons: List<Reason>? = null
) : Parcelable