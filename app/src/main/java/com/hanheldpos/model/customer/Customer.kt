package com.hanheldpos.model.customer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Customer(
    var Id: String,
    var _rev: String,
    var _key: String,
    var FirstName: String,
    var LastName: String,
    var FullName: String,
    var NameAcronymn: String,
    var Avatar: String,
    var CountryId: Int,
    var Phone: String,
    var Email: String,
    var Company: String,
    var ListGroups: String,
    var ReferenceId: String,
    var Birthday: String,
    var CityId: Int,
    var CityName: String,
    var AddressTypeId: Int,
    var Name: String,
    var Address1: String,
    var Address2: String,
    var DistrictId: Int,
    var DistrictName: String,
    var WardId: Int,
    var WardName: String,
    var Zip: String,
    var CreateDate: String,
    var Visible: Int,
    var UserGuid: String,
    var Latitude: Double,
    var Longitude: Double,
    var DialingCode: String
) : Parcelable {
}