package com.hanheldpos.data.api.pojo.device

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Region(
    val DisplayName: String,
    val EnglishName: String,
    val FullDateTimePattern: String,
    val LongDatePattern: String,
    val LongTimePattern: String,
    val ShortDatePattern: String,
    val ShortTimePattern: String,
    val DayNames: List<String>,
    val MonthNames: List<String>,
    val CurrencySymbol: String,
    val CurrencyDecimalDigits: Long?,
    val CurrencyDecimalSeparator: String,
    val CurrencyGroupSeparator: String,
    val NumberDecimalDigits: Long?,
    val NumberNegativePattern: Long?,
    val PerMilleSymbol: String,
    val PercentDecimalDigits: Long?,
    val PercentDecimalSeparator: String,
    val TextInfo: String,
    val ThreeLetterIsoLanguageName: String,
    val ThreeLetterWindowsLanguageName: String,
    val TwoLetterIsoLanguageName: String,
    val RegionTwoLetterIsoRegionName: String,
    val RegionThreeLetterWindowsRegionName: String,
    val RegionGeoId: Long?,
    val RegionName: String,
    val RegionDisplayName: String,
    val RegionNativeName: String,
    val NumericCode: Long?,
    val DialCodes: List<Long>,
) : Parcelable
