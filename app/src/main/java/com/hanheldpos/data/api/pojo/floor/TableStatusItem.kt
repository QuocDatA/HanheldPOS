package com.hanheldpos.data.api.pojo.floor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TableStatusItem(
    val BgColor: String,
    val Default: Int,
    val Id: Int,
    val OrderNo: Int,
    val Title_en: String,
    val Title_vi: String,
    val Visible: Int
) : Parcelable