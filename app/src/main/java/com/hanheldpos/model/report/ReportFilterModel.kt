package com.hanheldpos.model.report

import com.google.gson.annotations.SerializedName


data class ReportFilterModel(
    @SerializedName("CashDrawer_id")
    val cashDrawerId: String? = null,
    @SerializedName("CreateDate")
    val createDate: String? = null,
    @SerializedName("Device_id")
    val deviceId: String? = null,
    @SerializedName("Employee_id")
    val employeeId: String? = null,
    @SerializedName("EndDay")
    val endDay: String?,
    @SerializedName("EndHour")
    val endHour: String?,
    @SerializedName("IsAllDevice")
    val isAllDevice: Boolean?,
    @SerializedName("IsCurrentCashDrawer")
    val isCurrentCashDrawer: Boolean?,
    @SerializedName("IsRead")
    val isRead: Boolean? = null,
    @SerializedName("Location_id")
    val locationId: String? = null,
    @SerializedName("ReportResult")
    val reportResult: String? = null,
    @SerializedName("RequestDate")
    val requestDate: String? = null,
    @SerializedName("StartDay")
    var startDay: String?,
    @SerializedName("StartHour")
    val startHour: String?,
    @SerializedName("StatusId")
    val statusId: String? = null,
    @SerializedName("TotalDay")
    val totalDay: String? = null,
    @SerializedName("UpdateDate")
    val updateDate: String? = null,
    @SerializedName("User_id")
    val userId: String? = null,
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("_key")
    val key: String? = null,
)