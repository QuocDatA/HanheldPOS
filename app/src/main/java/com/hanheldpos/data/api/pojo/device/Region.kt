package com.hanheldpos.data.api.pojo.device

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Region(
    val CurrencyDecimalDigits: Int,
    val CurrencyDecimalSeparator: String,
    val CurrencyGroupSeparator: String,
    val CurrencySymbol: String,
    val DayNames: List<String>,
    val DialCodes: List<String>,
    val DisplayName: String,
    val EnglishName: String,
    val FullDateTimePattern: String,
    val LongDatePattern: String,
    val LongTimePattern: String,
    val MonthNames: List<String>,
    val NumberDecimalDigits: Int,
    val NumberNegativePattern: String,
    val NumericCode: Int,
    val PerMilleSymbol: String,
    val PercentDecimalDigits: Int,
    val PercentDecimalSeparator: String,
    val RegionDisplayName: String,
    val RegionGeoId: Int,
    val RegionName: String,
    val RegionNativeName: String,
    val RegionThreeLetterWindowsRegionName: String,
    val RegionTwoLetterISORegionName: String,
    val ShortDatePattern: String,
    val ShortTimePattern: String,
    val TextInfo: String,
    val ThreeLetterISOLanguageName: String,
    val ThreeLetterWindowsLanguageName: String,
    val TwoLetterISOLanguageName: String
) : Parcelable