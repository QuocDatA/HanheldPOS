package com.hanheldpos.data.api.pojo.setting.hardware


import com.google.gson.annotations.SerializedName

data class HardwareSetting(
    @SerializedName("PrinterList")
    val printerList: List<HardwarePrinter>?,
    @SerializedName("UserGuid")
    val userGuid: String?
)