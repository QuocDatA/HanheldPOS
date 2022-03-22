package com.hanheldpos.data.api.pojo.payment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WalletCardResp(
    val _id: String,
    val _rev: String,
    val _key: Int?,
    val CustomerGuid: String,
    val Receivable: Double?,
    val Payable: Double,
    val Balance: Double,
    val CreateDate: String?,
    val LastDate: String?,
    val ActiveDate: String?,
    val Active: Int?,
    val Locker: Int?,
    val LockerDate: String?,
    val Visible: Int,
    val UserGuid: String,
) : Parcelable