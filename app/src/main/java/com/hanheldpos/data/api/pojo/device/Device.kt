package com.hanheldpos.data.api.pojo.device

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Device(
    val Acronymn: String,
    val AppCode: String,
    val Color: String,
    val CreateDate: String,
    val DevMode: Int,
    val DeviceId: Int,
    val DeviceType: Int,
    val DisplayStyle: Int,
    val EnableLocation: Boolean,
    val EnableNotification: Boolean,
    val ExtraItemStyle: Int,
    val ExtraStyle: Int,
    val HexCode: String,
    val InDate: String,
    val Location: String,
    val LocationAddress: String?,
    val Nickname: String,
    val OutDate: String,
    val Phone: String?,
    val PrintKitchen: Int,
    val PrintStandard: Int,
//    val RequestNotification: Any,
    val ResolutionId: Int,
    val SecretKey: String,
    val ShowReceiptScreen: Int,
    val Status: Int,
    val Token: String?,
    val UserGuid: String,
    val Visible: Int,
    val _Id: String,
    val _key: Int,
    val _rev: String
) : Parcelable