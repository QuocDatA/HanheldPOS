package com.hanheldpos.model.report

import com.google.gson.annotations.SerializedName


data class ReportModel(
    @SerializedName("CashDrawer_id")
    val cashDrawerId: String?,
    @SerializedName("CreateDate")
    val createDate: String?,
    @SerializedName("Device_id")
    val deviceId: String?,
    @SerializedName("Employee_id")
    val employeeId: String?,
    @SerializedName("EndDay")
    val endDay: String?,
    @SerializedName("EndHour")
    val endHour: String?,
    @SerializedName("IsAllDevice")
    val isAllDevice: Boolean?,
    @SerializedName("IsCurrentCashDrawer")
    val isCurrentCashDrawer: Boolean?,
    @SerializedName("IsRead")
    val isRead: Boolean?,
    @SerializedName("Location_id")
    val locationId: String?,
    @SerializedName("ReportResult")
    val reportResult: String?,
    @SerializedName("RequestDate")
    val requestDate: String?,
    @SerializedName("StartDay")
    val startDay: String?,
    @SerializedName("StartHour")
    val startHour: String?,
    @SerializedName("StatusId")
    val statusId: String?,
    @SerializedName("TotalDay")
    val totalDay: String?,
    @SerializedName("UpdateDate")
    val updateDate: String?,
    @SerializedName("User_id")
    val userId: String?,
    @SerializedName("_id")
    val id: String?,
    @SerializedName("_key")
    val key: String?,
)