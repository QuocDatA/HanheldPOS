package com.hanheldpos.data.api.pojo.device

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingsId(
    val Acronymn: String,
    val CreateDate: String,
    val DeviceGuid: String,
    val Device_key: String,
    val LastUpdateDate: String,
    val LocationGuid: String,
    val MaxChar: Int,
    val MinimumNumber: Int,
    val Number: Int,
    val NumberFormat: String,
    val NumberIncrement: String,
    val Prefix: String,
    val Preview: String,
    val SettingId: Int,
    val SettingTypeId: Int,
    val UserGuid: String,
    val _id: String,
    val _key: String,
    val _rev: String
) : Parcelable