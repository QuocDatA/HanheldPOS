package com.hanheldpos.data.api.pojo.payment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GiftCardResp(
    val Active: Int,
    val Balance: Double,
    val CardNumber: String,
    val CreateDate: String,
    val Discount: Double,
    val ExpDate: String,
    val LinePrice: Double,
    val LocationGuid: String?,
    val Payable: Double,
    val Receivable: Double,
    val UserGuid: String,
    val Visible: Int,
    val _id: String,
    val _key: Int,
    val _rev: String
) : Parcelable