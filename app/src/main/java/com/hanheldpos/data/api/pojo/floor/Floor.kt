package com.hanheldpos.data.api.pojo.floor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Floor(
    val CreateDate: String,
    val Description: String?,
    val DiningOptionId: Int,
    val FloorCode: String,
    val FloorId: Int,
    val Handle: String,
    val LocationGuid: String,
    val Name: String,
    val OrderNo: Int,
    val PriceList: List<PriceFloor>,
    val UserGuid: String,
    val Visible: Int,
    val _Id: String,
    val _key: Int,
    val _rev: String
) : Parcelable