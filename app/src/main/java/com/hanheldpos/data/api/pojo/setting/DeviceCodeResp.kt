package com.hanheldpos.data.api.pojo.setting

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hanheldpos.data.api.pojo.employee.EmployeeResp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeviceCodeResp(

    @field:SerializedName("Message")
    val message: String? = null,

    @field:SerializedName("PageSize")
    val pageSize: Int? = null,

    @field:SerializedName("PageCount")
    val pageCount: Double? = null,

    @field:SerializedName("PageNumber")
    val pageNumber: Int? = null,

    @field:SerializedName("Model")
    val model: List<ModelItem?>? = null,

    @field:SerializedName("ErrorMessage")
    val errorMessage: String? = null,

    @field:SerializedName("ItemsCount")
    val itemsCount: Int? = null,

    @field:SerializedName("DidError")
    val didError: Boolean? = null,

    ) : Parcelable

@Parcelize
data class ViewItemModeItem(

    @field:SerializedName("ViewMode")
    val viewMode: List<ViewModeItem?>? = null,

    @field:SerializedName("PictureMode")
    val pictureMode: List<PictureModeItem?>? = null
) : Parcelable

@Parcelize
data class ModelItem(

    @field:SerializedName("ViewItemMode")
    val viewItemMode: List<ViewItemModeItem?>? = null,

    @field:SerializedName("SystemTimeZoneInfo")
    val systemTimeZoneInfo: List<SystemTimeZoneInfoItem?>? = null,

    @field:SerializedName("domain_images")
    val domainImages: String? = null,

    @field:SerializedName("Device")
    val device: List<DeviceItem?>? = null,

    @field:SerializedName("Region")
    val region: List<RegionItem?>? = null,

    @field:SerializedName("Employees")
    val employees: List<EmployeeResp?>? = null,

    @field:SerializedName("Users")
    val users: Users? = null,

    @field:SerializedName("ListDeviceType")
    val listDeviceType: List<ListDeviceTypeItem?>? = null,

    @field:SerializedName("DeviceInfoGuid")
    val deviceInfoGuid: String? = null
) : Parcelable

@Parcelize
data class PictureModeItem(

    @field:SerializedName("Title_en")
    val titleEn: String? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("Id")
    val id: Int? = null,

    @field:SerializedName("Default")
    val default: Int? = null,

    @field:SerializedName("Title_vi")
    val titleVi: String? = null,

    @field:SerializedName("Handle")
    val handle: String? = null
) : Parcelable

@Parcelize
data class SystemTimeZoneInfoItem(

    @field:SerializedName("TimeZoneInfoDate")
    val timeZoneInfoDate: String? = null,

    @field:SerializedName("TimeZoneInfoMyTimeFormat")
    val timeZoneInfoMyTimeFormat: String? = null,

    @field:SerializedName("SystemTimeZone")
    val systemTimeZone: String? = null,

    @field:SerializedName("TimeZoneInfoTime")
    val timeZoneInfoTime: String? = null,

    @field:SerializedName("DaylightName")
    val daylightName: String? = null,

    @field:SerializedName("TimeZoneInfoNow")
    val timeZoneInfoNow: String? = null,

    @field:SerializedName("BaseUtcOffset")
    val baseUtcOffset: String? = null,

    @field:SerializedName("DisplayName")
    val displayName: String? = null,

    @field:SerializedName("SupportsDaylightSavingTime")
    val supportsDaylightSavingTime: Boolean? = null,

    @field:SerializedName("TimeZoneInfoDetail")
    val timeZoneInfoDetail: String? = null,

    @field:SerializedName("Id")
    val id: String? = null,

    @field:SerializedName("TimeZoneInfoMyDayFormat")
    val timeZoneInfoMyDayFormat: String? = null,

    @field:SerializedName("StandardName")
    val standardName: String? = null
) : Parcelable

@Parcelize
data class ViewModeItem(

    @field:SerializedName("Title_en")
    val titleEn: String? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("Id")
    val id: Int? = null,

    @field:SerializedName("Default")
    val default: Int? = null,

    @field:SerializedName("Title_vi")
    val titleVi: String? = null,

    @field:SerializedName("Handle")
    val handle: String? = null
) : Parcelable

@Parcelize
data class DeviceItem(

    @field:SerializedName("Status")
    val status: Int? = null,

    @field:SerializedName("RequestNotification")
    val requestNotification: String? = null,

    @field:SerializedName("UserGuid")
    val userGuid: String? = null,

    @field:SerializedName("DeviceId")
    val deviceId: Int? = null,

    @field:SerializedName("_rev")
    val rev: String? = null,

    @field:SerializedName("OutDate")
    val outDate: String? = null,

    @field:SerializedName("_key")
    val key: Int? = null,

    @field:SerializedName("EnableLocation")
    val enableLocation: String? = null,

    @field:SerializedName("CreateDate")
    val createDate: String? = null,

    @field:SerializedName("AppCode")
    val appCode: String? = null,

    @field:SerializedName("DeviceType")
    val deviceType: Int? = null,

    @field:SerializedName("InDate")
    val inDate: String? = null,

    @field:SerializedName("EnableNotification")
    val enableNotification: String? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("_Id")
    val id: String? = null,

    @field:SerializedName("HexCode")
    val hexCode: String? = null,

    @field:SerializedName("Nickname")
    val nickname: String? = null,

    @field:SerializedName("Location")
    val location: String? = null,

    @field:SerializedName("ResolutionId")
    val resolutionId: Int? = null,

    @field:SerializedName("PrintStandard")
    val printStandard: Int? = null,

    @field:SerializedName("PrintKitchen")
    val printKitchen: Int? = null,

    @field:SerializedName("ShowReceiptScreen")
    val showReceiptScreen: Int? = null,

    //field for New UI variant & modifier

    @field:SerializedName("DisplayStyle")
    val styleUI: Int = 1,

    @field:SerializedName("ExtraStyle")
    val isModifierNewPage: Int = 1,

    val isComboSuggestion: Boolean = false,

    @field:SerializedName("ExtraItemStyle")
    val modifierStyle: Int = 2,


) : Parcelable

@Parcelize
data class RegionItem(

    @field:SerializedName("MonthNames")
    val monthNames: List<String?>? = null,

    @field:SerializedName("ThreeLetterWindowsLanguageName")
    val threeLetterWindowsLanguageName: String? = null,

    @field:SerializedName("DialCodes")
    val dialCodes: List<String?>? = null,

    @field:SerializedName("ThreeLetterISOLanguageName")
    val threeLetterISOLanguageName: String? = null,

    @field:SerializedName("NumericCode")
    val numericCode: Int? = null,

    @field:SerializedName("LongTimePattern")
    val longTimePattern: String? = null,

    @field:SerializedName("CurrencySymbol")
    val currencySymbol: String? = null,

    @field:SerializedName("NumberDecimalDigits")
    val numberDecimalDigits: Int? = null,

    @field:SerializedName("TwoLetterISOLanguageName")
    val twoLetterISOLanguageName: String? = null,

    @field:SerializedName("RegionNativeName")
    val regionNativeName: String? = null,

    @field:SerializedName("DayNames")
    val dayNames: List<String?>? = null,

    @field:SerializedName("LongDatePattern")
    val longDatePattern: String? = null,

    @field:SerializedName("RegionName")
    val regionName: String? = null,

    @field:SerializedName("RegionTwoLetterISORegionName")
    val regionTwoLetterISORegionName: String? = null,

    @field:SerializedName("RegionDisplayName")
    val regionDisplayName: String? = null,

    @field:SerializedName("DisplayName")
    val displayName: String? = null,

    @field:SerializedName("TextInfo")
    val textInfo: String? = null,

    @field:SerializedName("RegionThreeLetterWindowsRegionName")
    val regionThreeLetterWindowsRegionName: String? = null,

    @field:SerializedName("PercentDecimalDigits")
    val percentDecimalDigits: Int? = null,

    @field:SerializedName("ShortTimePattern")
    val shortTimePattern: String? = null,

    @field:SerializedName("CurrencyDecimalDigits")
    val currencyDecimalDigits: Int? = null,

    @field:SerializedName("RegionGeoId")
    val regionGeoId: Int? = null,

    @field:SerializedName("ShortDatePattern")
    val shortDatePattern: String? = null,

    @field:SerializedName("CurrencyGroupSeparator")
    val currencyGroupSeparator: String? = null,

    @field:SerializedName("PerMilleSymbol")
    val perMilleSymbol: String? = null,

    @field:SerializedName("CurrencyDecimalSeparator")
    val currencyDecimalSeparator: String? = null,

    @field:SerializedName("PercentDecimalSeparator")
    val percentDecimalSeparator: String? = null,

    @field:SerializedName("FullDateTimePattern")
    val fullDateTimePattern: String? = null,

    @field:SerializedName("NumberNegativePattern")
    val numberNegativePattern: String? = null,

    @field:SerializedName("EnglishName")
    val englishName: String? = null
) : Parcelable

@Parcelize
data class ListDeviceTypeItem(

    @field:SerializedName("Title")
    val title: String? = null,

    @field:SerializedName("Id")
    val id: Int? = null
) : Parcelable

@Parcelize
data class Users(

    @field:SerializedName("TimeZoneDisplayName")
    val timeZoneDisplayName: String? = null,

    @field:SerializedName("BusinessName")
    val businessName: String? = null,

    @field:SerializedName("Language")
    val language: String? = null,

    @field:SerializedName("CustomerCode")
    val customerCode: String? = null,

    @field:SerializedName("CurrencyNativeName")
    val currencyNativeName: String? = null,

    @field:SerializedName("CurrencyId")
    val currencyId: String? = null,

    @field:SerializedName("CurrencySymbol")
    val currencySymbol: String? = null
) : Parcelable
