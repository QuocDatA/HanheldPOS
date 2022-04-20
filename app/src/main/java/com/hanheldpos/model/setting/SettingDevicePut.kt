package com.hanheldpos.model.setting

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingDevicePut(
    val MaxChar : Long,
    val NumberIncrement: String,
    val UserGuid: String,
    val LocationGuid: String,
    val DeviceGuid : String,
    val Device_key : String,
    val uuid : String,
) : Parcelable
