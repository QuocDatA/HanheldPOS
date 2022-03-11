package com.hanheldpos.data.api.pojo.device

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SystemSettingsType(
    val Default: Int,
    val Description: String,
    val Id: Int,
    val Title: String,
    val Visible: Int,
    val _id: String,
    val _key: String,
    val _rev: String
) : Parcelable