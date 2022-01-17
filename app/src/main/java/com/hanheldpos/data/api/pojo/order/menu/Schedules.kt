package com.hanheldpos.data.api.pojo.order.menu

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Schedules(
    val Day: String,
    val Id: Int,
    val OrderNo: Int,
    val Time: List<Time>,
    val Visible: Int
) : Parcelable