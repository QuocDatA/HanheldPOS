package com.hanheldpos.data.api.pojo.customer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomerResp(
    val Acronymn: String,
    val Address1: String,
    val Address2: String,
    val AddressTypeId: Int,
    val AddressTypeName: String?,
    val Avatar: String,
    val Birthday: String,
    val CityId: Int,
    val CityName: String,
    val Company: String,
    val CountryId: Int,
    val CreateDate: String,
    val DialingCode: String,
    val DistrictId: Int,
    val DistrictName: String,
    val Email: String,
    val EmailVerified: Boolean,
    val FirstName: String,
    val FullName: String,
    val FullName_Ansi: String,
    val Gender: Int,
    val IsLinkedApple: Boolean,
    val IsLinkedFacebook: Boolean,
    val IsLinkedGithub: Boolean,
    val IsLinkedGoogle: Boolean,
    val IsLinkedMicrosoft: Boolean,
    val IsLinkedTwitter: Boolean,
    val IsSignIn: Boolean,
    val IsVerifyEmail: Boolean,
    val IsVerifyPhone: Boolean,
    val LastName: String,
    val Latitude: Boolean,
//    val ListGroups: Any,
    val Longitude: Boolean,
    val Name: String,
    val NameAcronymn: String,
    val NickName: String,
    val Note: String?,
    val Phone: String,
//    val PhoneVerified: Any,
    val ReferenceId: String,
    val ReferenceId2: String,
    val Token: String?,
    val UserGuid: String,
    val Visible: Int,
    val WardId: Int,
    val WardName: String,
    val Zip: String,
    val _Id: String,
    val _key: Int,
    val _rev: String,
//    val grant_type: Any
) : Parcelable {
    fun getFullAddressWithLineBreaker(): String {
        return getFullAddress(true)
    }

    private fun getFullAddress(isLineBreaked: Boolean = false): String {

        return StringBuilder().apply {
            if (Address1.isNotEmpty()) {
                append(Address1)
                if (isLineBreaked) {
                    append("\n")
                } else {
                    append(", ")
                }
            }
            if (Address2.isNotEmpty()) {
                append(Address2)
                if (isLineBreaked) {
                    append("\n")
                } else {
                    append(", ")
                }
            }
            if (WardName.isNotEmpty()) {
                append(WardName)
                if (isLineBreaked) {
                    append("\n")
                } else {
                    append(", ")
                }
            }
            if (CityName.isNotEmpty()) {
                append(CityName)
                if (isLineBreaked) {
                    append("\n")
                } else {
                    append(", ")
                }
            }
            if (Company?.isNotEmpty()) {
                if (isLineBreaked) {
                    append("\n")
                } else {
                    append(", ")
                }
                append(Company)
            }
        }.toString()
    }
}