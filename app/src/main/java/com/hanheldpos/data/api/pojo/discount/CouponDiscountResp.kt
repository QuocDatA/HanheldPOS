package com.hanheldpos.data.api.pojo.discount

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CouponDiscountResp(
    @SerializedName("ListLineDiscount")
    val ProductDiscountList: List<DiscountCoupon>,

    @SerializedName("OrderDiscount")
    val OrderDiscountList: List<DiscountCoupon>,

    @SerializedName("DiscountUsed")
    val DiscountUsed: List<DiscountUsed>,
) : Parcelable {
}