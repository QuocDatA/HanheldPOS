package com.hanheldpos.data.api.pojo.floor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriceFloor(
    val Id: Int,
    val IsDefault: Boolean,
    val Location: String,
    val Name: String
) : Parcelable