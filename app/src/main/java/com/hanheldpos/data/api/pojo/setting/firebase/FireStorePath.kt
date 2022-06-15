package com.hanheldpos.data.api.pojo.setting.firebase


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FireStorePath(
    @SerializedName("AppVersion")
    val appVersion: String?,
    @SerializedName("DataVersion")
    val dataVersion: String?,
    @SerializedName("LocationList")
    val locationList: String?,
    @SerializedName("OrderList")
    val orderList: String?,
    @SerializedName("ProductList")
    val productList: String?,
    @SerializedName("ReportList")
    val reportList: String?,
    @SerializedName("Transaction")
    val transaction: String?
) : Parcelable