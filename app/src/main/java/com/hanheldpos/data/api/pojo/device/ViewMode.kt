package com.hanheldpos.data.api.pojo.device

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ViewMode(
    val Default: Int,
    val Group: Int,
    val Handle: String,
    val Id: Int,
    val OrderNo: Int,
    val Title_en: String,
    val Title_vi: String,
    val Visible: Int,
    val _id: String,
    val _key: Int,
    val _rev: String
) : Parcelable