package com.hanheldpos.data.api.pojo.discount

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.product.ProductItem
import com.hanheldpos.data.api.pojo.product.VariantsGroup
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountResp(
    val DidError: Boolean,
    val ErrorMessage: String?,
    val Message: String?,
    val Model: List<DiscountItem>
) : Parcelable

@Parcelize
data class DiscountItem(
    val Acronymn: String,
    val ApplyToDiningOptionText: String,
    val ApplyToPriceProduct: Int,
    val AssignTo: Int,
    val Color: String,
    val Condition: Condition,
    val CustomerEligibility: Int,
    val CustomerEligibilityList: List<CustomerEligibility>,
    val DateOff: String,
    val DateOn: String,
    val DateRange: Int,
    val Description: String,
    val DiningOption: List<DiningOptionDiscount>,
    val DiscountApplyTo: Int,
    val DiscountAutomatic: Boolean,
    val DiscountAutomaticText: String,
    val DiscountCode: String,
    val DiscountName: String,
    val DiscountText: String,
    val DiscountType: Int,
    val DiscountTypeText: String,
    val DiscountValueText: String,
    val DiscountsApplyToItem: DiscountsApplyToItem,
    val Excluding: Int,
    val FromToDiscount: String,
    val IsFullBanner: Boolean,
    val IsVisiblePOS: Int,
    val MaximumDiscount: Double,
    val MaximumDiscountFormatter: String,
    val MaximumNumberOfUsedPerOrder: Boolean,
    val MaximumNumberOfUsedPerOrderValue: Int,
    val MinimumOrder: Double,
    val MinimumOrderFormatter: String,
    val MinimumOrderId: Int,
    val MinimumRequiredType: Int,
    val MinimumRequiredValue: Double,
    val OnlyApplyDiscountOncePerOrder: Int,
    val OnlyApplyDiscountProductOncePerOrder: Int,
    val OrderNo: Int,
    val ScheduleList: List<ListScheduleItem>,
    val SetSchedules: Int,
    val Trigger: List<Trigger>,
    val Url: String,
    val UsageLimits: String,
    val UseForOrder: Int,
    val _id: String,
    val jsaction: String
) : Parcelable

@Parcelize
data class Condition(
    val CustomerBuys: CustomerBuys,
    val DiscountValue: Double
) : Parcelable

@Parcelize
data class DiningOptionDiscount(
    val Id: Int
) : Parcelable

@Parcelize
data class CustomerEligibility(
    val _id: String
) : Parcelable

@Parcelize
data class CustomerBuys(
    val ApplyTo: Int,
    val IsDiscountLimit: Int,
    val IsMaxAmount: Int,
    val IsMaxQuantity: Int,
    val ListApplyTo: List<ProductItem>,
    val MaximumDiscount: Double
) : Parcelable

@Parcelize
data class DiscountsApplyToItem(
    val ApplyTo: Int,
    val DiscountAutomatic: Boolean,
    val DiscountGuid: String,
    val DiscountType: Int,
    val ListCombo: String,
    val ListParents: String,
    val ListProducts: String,
    val UserGuid: String
) : Parcelable


@Parcelize
data class Trigger(
    val Id: Int,
    val Name: String,
    val RefreshTimer: Int
) : Parcelable

@Parcelize
data class ListScheduleItem(
    val Id: String,
    val Date: String,
    val ListSetTime: List<ListSetTimeItem>
) : Parcelable

@Parcelize

data class ListSetTimeItem(
    val TimeOn: String,
    val TimeOff: String,
    val OrderNo: Long,
) : Parcelable