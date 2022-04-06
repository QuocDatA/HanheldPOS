package com.hanheldpos.data.api.pojo.discount

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CouponDiscountReq(
    val Source: Int ? = null,

    val CouponCode: String,

    val CustomerGuestGuid: String? = "",

    val DeliveryTypeId: Int?,

    val DeviceGuid: String,

    val UserGuid: String,

    val LocationGuid: String,

    val DiscountUsed: List<DiscountUsed>,

    val OrderDetail: List<OrderDetailPostCoupon>
) : Parcelable {

}

@Parcelize
data class OrderDetailPostCoupon(
    val ProductsBuy: List<ProductsBuyPostCoupon>,

    val ListLineExtra: List<ListLineExtraPostCoupon>? = null
) : Parcelable {
}

@Parcelize
data class ListLineExtraPostCoupon(
    val ExtraId: String,

    val ModifierGuid: String,

    val Quantity: Int?
) : Parcelable {
}

@Parcelize
data class ProductsBuyPostCoupon(
    val ExtraId: String,

    val Note: String? = null,

    val DiscountUsed: List<DiscountUsed> ? = null,

    val Active: Int?,

    val OrderDetailType: Int?,

    val ProductGuid: String,

    val Quantity: Int?,

    val Variants: String
) : Parcelable {
}
