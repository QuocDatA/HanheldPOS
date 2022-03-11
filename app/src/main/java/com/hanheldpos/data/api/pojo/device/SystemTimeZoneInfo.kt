package com.hanheldpos.data.api.pojo.device

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SystemTimeZoneInfo(
    val BaseUtcOffset: String,
    val DaylightName: String,
    val DisplayName: String,
    val Id: String,
    val StandardName: String,
    val SupportsDaylightSavingTime: Boolean,
    val SystemTimeZone: String,
    val TimeZoneInfoDate: String,
    val TimeZoneInfoDetail: String,
    val TimeZoneInfoMyDayFormat: String,
    val TimeZoneInfoMyTimeFormat: String,
    val TimeZoneInfoNow: String,
    val TimeZoneInfoTime: String
) : Parcelable