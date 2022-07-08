package com.hanheldpos.data.api.pojo.loyalty

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoyaltyReqModel(
    @SerializedName("UserGuid") var userGuid: String?,
    @SerializedName("OrderGuid") var orderGuid: String?,
    @SerializedName("LocationGuid") var locationGuid: String?,
    @SerializedName("DeviceGuid") var deviceGuid: String?,
    @SerializedName("EmployeeGuid") var employeeGuid: String?,
    @SerializedName("CustomerGuid") var customerGuid: String?,
    @SerializedName("GrandTotal") var grandTotal: Double?,

): Parcelable