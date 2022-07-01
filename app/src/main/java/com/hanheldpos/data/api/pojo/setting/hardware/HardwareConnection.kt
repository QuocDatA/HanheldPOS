package com.hanheldpos.data.api.pojo.setting.hardware


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HardwareConnection(
    @SerializedName("ConnectionTypeId")
    val connectionTypeId: Int?,
    @SerializedName("_id")
    var id: String?,
    @SerializedName("IsChecked")
    var isChecked: Boolean?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("Port")
    var port: String?
) : Parcelable