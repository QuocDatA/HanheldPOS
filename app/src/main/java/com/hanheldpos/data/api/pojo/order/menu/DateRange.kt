package com.hanheldpos.data.api.pojo.order.menu

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DateRange(
    val DateOn: String,
    val Id: Int,
    val OrderNo: Int,
    val TimeOn: String,
    val TitleDate_en: String,
    val TitleTime_en: String,
    val Visible: Int
) : Parcelable