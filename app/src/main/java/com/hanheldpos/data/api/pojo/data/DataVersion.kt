package com.hanheldpos.data.api.pojo.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataVersion(
    @SerializedName("AppVersion")
    val appVersion: String?,
    @SerializedName("Discount")
    val discount: Long?,
    @SerializedName("Menu")
    val menu: Long?
) : Parcelable