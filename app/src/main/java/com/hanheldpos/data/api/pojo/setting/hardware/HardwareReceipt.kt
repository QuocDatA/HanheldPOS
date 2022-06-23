package com.hanheldpos.data.api.pojo.setting.hardware


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HardwareReceipt(
    @SerializedName("_id")
    val id: String?,
    @SerializedName("Quantity")
    val quantity: Int?,
    @SerializedName("ReceiptTypeId")
    val receiptTypeId: Int?
) : Parcelable