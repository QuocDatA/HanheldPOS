package com.hanheldpos.model.cart.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Shipping(
    var _id: String,
    var FullName: String,
    var Phone: String,
    var AddressName: String,
    var StoreName: String,
    var WardName: String,
    var DistrictName: String,
    var CityName: String,
    var AddressTypeId: Long,
    var AddressTypeName: String,
    var Address1: String,
    var Address2: String,
    var Address3: String,
    var Note: String,
    var DistanceValue: Long,
    var DistanceText: String,
    var DurationValue: Long,
    var DurationText: String,
    var Latitude: Double,
    var Longitude: Double,
    var Company: String,
) : Parcelable {
}