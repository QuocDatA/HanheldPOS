package com.hanheldpos.data.api.pojo.setting.hardware


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HardwarePrinter(
    @SerializedName("ConnectionList")
    val connectionList: List<HardwareConnection>?,
    @SerializedName("Enable")
    val enable: Boolean?,
    @SerializedName("_id")
    val id: String?,
    @SerializedName("IsConnectCashDrawer")
    val isConnectCashDrawer: Boolean?,
    @SerializedName("Name")
    val name: String?,
    @SerializedName("OrderNo")
    val orderNo: Int?,
    @SerializedName("PrinterTypeId")
    val printerTypeId: Int?,
    @SerializedName("ReceiptList")
    val receiptList: List<HardwareReceipt>?,
    @SerializedName("Visible")
    val visible: Int?
) : Parcelable