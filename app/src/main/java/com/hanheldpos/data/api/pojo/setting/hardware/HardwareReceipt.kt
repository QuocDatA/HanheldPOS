package com.hanheldpos.data.api.pojo.setting.hardware


import com.google.gson.annotations.SerializedName

data class HardwareReceipt(
    @SerializedName("_id")
    val id: String?,
    @SerializedName("Quantity")
    val quantity: Int?,
    @SerializedName("ReceiptTypeId")
    val receiptTypeId: Int?
)