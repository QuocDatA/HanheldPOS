package com.hanheldpos.data.api.pojo.order.menu

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hierarchy(
//    val Children: List<Any>,
    val Color: String,
    val GroupGuid: String,
    val Level: Int,
    val MenusGroupGuid: String,
    val MenusGuid: String,
    val MenusType: Int,
    val OrderNo: Int,
    val Title: String,
    val Url: String,
    val Visible: Int
) : Parcelable