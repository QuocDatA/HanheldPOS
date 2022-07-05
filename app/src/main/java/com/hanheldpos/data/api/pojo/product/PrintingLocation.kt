package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class PrintingLocation(
    @field:SerializedName("Id")
    val id: Int? = null,

    @field:SerializedName("Title")
    val title: String? = null
) : Parcelable