package com.hanheldpos.data.api.pojo.setting.hardware


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HardwareSetting(
    @SerializedName("PrinterList")
    val printerList: List<HardwarePrinter>?,
    @SerializedName("UserGuid")
    val userGuid: String?
) : Parcelable