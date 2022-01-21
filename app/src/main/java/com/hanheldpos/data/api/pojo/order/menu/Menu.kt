package com.hanheldpos.data.api.pojo.order.menu

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Menu(
    val Acronymn: String,
    val Active: Int,
    val Ansi: String?,
    val AsignTo: Int,
    val AvailableFutureLocation: Int,
    val Handle: String,
    val Location: String,
    val Name: String,
    val OrderNo: Int,
    val UrlNormal: String?,
    val UrlSelected: String?,
    val UserGuid: String,
    val Visible: Int,
    val _Id: String,
    val _key: Int,
    val _rev: String
) : Parcelable