package com.hanheldpos.data.api.pojo.loyalty

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoyaltyResp(
    @SerializedName("UserGuid") var userGuid: String?,
    @SerializedName("OrderGuid") var orderGuid: String?,
    @SerializedName("LocationGuid") var locationGuid: String?,
    @SerializedName("DeviceGuid") var deviceGuid: String?,
    @SerializedName("EmployeeGuid") var employeeGuid: String?,
    @SerializedName("CustomerGuid") var customerGuid: String?,
    @SerializedName("EarnId") var earnId: Int?,
    @SerializedName("Receivable") var receivable: Double?,
    @SerializedName("Description1") var description1: String?,
    @SerializedName("Description2") var description2: String?,
    @SerializedName("Points") var point: Double?,
    @SerializedName("PointsText") var pointsText: String?,
) : Parcelable