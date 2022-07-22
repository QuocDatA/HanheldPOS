package com.hanheldpos.data.api.pojo.fee

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.discount.DiningOptionDiscount
import kotlinx.parcelize.Parcelize

@Parcelize
data class Discount(
    val Acronymn: String,
    val ApplyToPriceProduct: Int,
    val AssignTo: Int,
    val Color: String,
    val Condition: Condition,
    val CustomerEligibility: Int,
    val DateOff: String,
    val DateOn: String,
    val DateRange: Int,
    val DiningOption: List<DiningOptionDiscount>,
    val DiscountApplyToOrder: Int,
    val DiscountAutomatic: Boolean,
    val DiscountCode: String,
    val DiscountName: String,
    val DiscountType: Int,
    val DiscountsApplyToItem: DiscountsApplyToItem,
    val Excluding: Int,
    val IsVisiblePOS: Int,
    val ListCustomerEligibility: String,
    val ListSchedules: String,
//    val ListUrl: List<Any>,
    val MinimumRequired: String,
    val MinimumRequiredType: Int,
    val OnlyApplyDiscountOncePerOrder: Int,
    val OnlyApplyDiscountProductOncePerOrder: Int,
    val SetSchedules: Int,
    val UsageLimits: String,
    val _id: String
) : Parcelable