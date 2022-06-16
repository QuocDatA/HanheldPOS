package com.hanheldpos.data.api.pojo.setting.hardware


import com.google.gson.annotations.SerializedName

data class HardwareConnection(
    @SerializedName("ConnectionTypeId")
    val connectionTypeId: Int?,
    @SerializedName("_id")
    val id: String?,
    @SerializedName("IsChecked")
    val isChecked: Boolean?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("Port")
    val port: String?
)