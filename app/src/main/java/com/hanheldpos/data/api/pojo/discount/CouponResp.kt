package com.hanheldpos.data.api.pojo.discount

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CouponResp(
    val DidError: Boolean,
    val ErrorMessage: String?,
    val Message: String?,
    val Model: List<DiscountDetail>
) : Parcelable

@Parcelize
data class DiscountDetail(
    val CombineValue: String,
    val CustomerEligibilityValue: String,
    val Description: String,
    val DiningOptionList: List<DiningOptionDiscount>,
    val DiningOptionValue: String,
    val DiscountLimitValue: String,
    val DiscountName: String,
    val DiscountType: Int,
    val DiscountTypeText: String,
    val DiscountValueFormatter: String,
    val MinimumRequirementValue: String,
    val RequirementProductList: List<RequirementProduct>,
    val RequirementTitle: String,
    val RequirementValue: String,
    val RewardProductList: List<RewardProduct>,
    val RewardTitle: String,
    val RewardValue: String,
//    val ScheduleList: List<Any>,
    val ScheduleValue: String,
    val TermsCondition: String,
    val Url: String,
//    val UsageLimitList: List<Any>,
    val UsageLimitValue: String,
    val UseSchedule: Boolean,
    val ValidDateValue: String,
    val _id: String
) : Parcelable

@Parcelize
data class RequirementProduct(
    val DiscountGuid: String,
    val Name1: String,
    val Sku: String,
    val Url: String,
    val _id: String
) : Parcelable

@Parcelize
data class RewardProduct(
    val DiscountGuid: String,
    val Name1: String,
    val Sku: String,
    val Url: String,
    val _id: String
) : Parcelable