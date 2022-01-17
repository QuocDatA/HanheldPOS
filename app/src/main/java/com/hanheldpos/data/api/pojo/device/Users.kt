package com.hanheldpos.data.api.pojo.device

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
    val BusinessName: String,
    val CurrencyId: String,
    val CurrencyNativeName: String,
    val CurrencySymbol: String,
    val CustomerCode: String,
    val PassCode: String?,
    val TimeZoneDisplayName: String,
    val TimeZoneId: String,
    val _id: String,
    val _key: Int,
    val _rev: String
) : Parcelable