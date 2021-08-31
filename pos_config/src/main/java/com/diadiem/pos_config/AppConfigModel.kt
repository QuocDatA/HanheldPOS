package com.diadiem.pos_config

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppConfigModel(

	@field:SerializedName("enable_login_method_google")
	val enableLoginMethodGoogle: Boolean? = null,

	@field:SerializedName("environment_key")
	val environmentKey: String? = null,

	@field:SerializedName("enable_login_method_apple")
	val enableLoginMethodApple: Boolean? = null,

	@field:SerializedName("enable_screen_login_first_time")
	val enableScreenLoginFirstTime: Boolean? = null,

	@field:SerializedName("enable_login_method_phone_number")
	val enableLoginMethodPhoneNumber: Boolean? = null,

	@field:SerializedName("enable_screen_choose_language")
	val enableScreenChooseLanguage: Boolean? = null,

	@field:SerializedName("google_place_service_key")
	val googlePlaceServiceKey: String? = null,

	@field:SerializedName("google_service_key")
	val googleServiceKey: String? = null,

	@field:SerializedName("enable_screen_intro")
	val enableScreenIntro: Boolean? = null,

	@field:SerializedName("base_domain_api")
	val baseDomainApi: String? = null,

	@field:SerializedName("enable_log")
	val enableLog: Boolean? = null,

	@field:SerializedName("category_item_expand_base_on_text")
	val categoryItemExpandBaseOnText: Boolean? = null,

	@field:SerializedName("styles")
	val styles: Styles? = null,

	@field:SerializedName("enable_login_method_facebook")
	val enableLoginMethodFacebook: Boolean? = null,

	@field:SerializedName("facebook_service_key")
	val facebookServiceKey: String? = null
) : Parcelable

@Parcelize
data class Styles(

	@field:SerializedName("fonts")
	val fonts: Fonts? = null,

	@field:SerializedName("colors")
	val colors: Colors? = null
) : Parcelable

@Parcelize
data class Sizes(

	@field:SerializedName("h1")
	val h1: Double? = null,

	@field:SerializedName("h2")
	val h2: Double? = null,

	@field:SerializedName("h3")
	val h3: Double? = null,

	@field:SerializedName("h4")
	val h4: Double? = null,

	@field:SerializedName("h5")
	val h5: Double? = null,

	@field:SerializedName("h6")
	val h6: Double? = null,

	@field:SerializedName("h7")
	val h7: Double? = null
) : Parcelable

@Parcelize
data class Colors(

	@field:SerializedName("color_4")
	val color4: String? = null,

	@field:SerializedName("color_5")
	val color5: String? = null,

	@field:SerializedName("color_6")
	val color6: String? = null,

	@field:SerializedName("color_7")
	val color7: String? = null,

	@field:SerializedName("color_8")
	val color8: String? = null,

	@field:SerializedName("color_9")
	val color9: String? = null,

	@field:SerializedName("color_0")
	val color0: String? = null,

	@field:SerializedName("color_1")
	val color1: String? = null,

	@field:SerializedName("color_2")
	val color2: String? = null,

	@field:SerializedName("color_3")
	val color3: String? = null,

	@field:SerializedName("color_10")
	val color10: String? = null,

	@field:SerializedName("color_11")
	val color11: String? = null,

	@field:SerializedName("color_12")
	val color12: String? = null,

	@field:SerializedName("color_13")
	val color13: String? = null,

	@field:SerializedName("color_14")
	val color14: String? = null,

	@field:SerializedName("color_15")
	val color15: String? = null,
) : Parcelable

@Parcelize
data class Families(

	@field:SerializedName("normal")
	val normal: String? = null,

	@field:SerializedName("bold")
	val bold: String? = null,

	@field:SerializedName("italic")
	val italic: String? = null,

	@field:SerializedName("fileType")
	val fileType: String? = null
) : Parcelable

@Parcelize
data class Fonts(

	@field:SerializedName("sizes")
	val sizes: Sizes? = null,

	@field:SerializedName("families")
	val families: Families? = null
) : Parcelable
