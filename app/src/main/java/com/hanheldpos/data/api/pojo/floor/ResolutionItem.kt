package com.hanheldpos.data.api.pojo.floor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResolutionItem(
    val Height: Double,
    val Name: String,
    val Padding: String,
    val Resolution: String,
    val ScaleH: Double,
    val ScaleL: Double,
    val ScaleT: Double,
    val ScaleW: Double,
    val Visible: Int,
    val Width: Double
) : Parcelable