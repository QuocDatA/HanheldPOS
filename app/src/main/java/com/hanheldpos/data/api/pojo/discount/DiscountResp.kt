package com.hanheldpos.data.api.pojo.discount

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.order.menu.ProductItem
import com.hanheldpos.data.api.pojo.order.menu.VariantsGroup
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountResp(
    val DidError: Boolean,
    val ErrorMessage: String?,
    val Message: String?,
    val Model: List<DiscountModel>
) : Parcelable  {
    @Parcelize
    data class DiscountModel(
        val Acronymn: String,
        val ApplyToDiningOptionText: String,
        val ApplyToPriceProduct: Int,
        val AssignTo: Int,
        val Color: String,
        val Condition: ConditionDiscount,
        val CustomerEligibility: Int,
        val DateOff: String,
        val DateOn: String,
        val DateRange: Int,
        val Description: String,
        val DiningOption: List<DiningOptionDiscount>,
        val DiscountAutomatic: Boolean,
        val DiscountAutomaticText: String,
        val DiscountCode: String,
        val DiscountName: String,
        val DiscountText: String,
        val DiscountType: Int,
        val DiscountTypeText: String,
        val DiscountValueText: String,
        val Excluding: Int,
        val FromToDiscount: String,
        val IsFullBanner: Boolean,
        val IsVisiblePOS: Int,
        val ListCustomerEligibility: String,
        val ListSchedules: String,
        val MaximumDiscount: Double,
        val MaximumDiscountFormatter: String,
        val MaximumNumberOfUsedPerOrder: Boolean,
        val MaximumNumberOfUsedPerOrderValue: Int,
        val MinimumOrder: Double,
        val MinimumOrderFormatter: String,
        val MinimumOrderId: Int,
        val MinimumRequired: String,
        val MinimumRequiredType: Int,
        val OnlyApplyDiscountApplyTo: Int,
        val OnlyApplyDiscountOncePerOrder: Int,
        val OrderNo: Int,
        val SetSchedules: Int,
        val Url: String,
        val UsageLimits: String,
        val Watch: String,
        val _id: String,
        val jsaction: String
    ) : Parcelable {

    }
}

@Parcelize
data class DiningOptionDiscount(
    val Id: Int
) : Parcelable

@Parcelize
data class ConditionDiscount(
    val AppliesTo: Int,
    val CustomerBuys: CustomerBuysModel?,
    val CustomerGets: CustomerGetsModel?,
    val DiscountGuid: String,
    val DiscountType: Int,
    val DiscountValue: Double,
    val IsDiscountLimit: Int,
    val IsMaxAmount: Int,
    val IsMaxQuantity: Int,
    val ListApplyTo: List<ListApplyToItem>,
    val ListFromItem: String,
    val MaximumDiscount: Double,
    val MaximumNumberOfUses: Int,
    val MaximumNumberOfUsesValue: Int,
    val _Id: String,
    val _key: Int,
    val _rev: String
) : Parcelable {
    @Parcelize
    data class CustomerGetsModel(
        val ApplyTo: Int,
        val CustomerName: String,
        val DiscountValue: Double,
        val DiscountValueType: Int,
        val Handle: String,
        val ListApplyTo: List<ListApplyToItem>,
        val ProductApplyTo: Int,
        val Quantity: Int
    ) : Parcelable

    @Parcelize
    data class CustomerBuysModel(
        val ApplyTo: Int,
        val CustomerName: String,
        val Handle: String,
        val ListApplyTo: List<ListApplyToItem>,
        val MinimumTypeId: Int,
        val MinimumValue: Double,
        val MinimumValueFormat: String,
        val ProductApplyTo: Int
    ) : Parcelable

    @Parcelize
    data class ListApplyToItem(
        val ApplyTo: Int,
        val ApplyToModifier: Int,
        val CategoryGuid: String,
        val Color: String,
        val Description1: String,
        val Description2: String,
//        val Discount: Any,
        val Handle: String,
        val IsLock: String,
        val IsMaxAmount: Int,
        val IsMaxQuantity: Int,
        val MaxAmount: Double,
        val MaxQuantity: Int,
        val Name1: String,
        val Name2: String,
        val Name3: String,
        val Name4: String,
        val Parent_id: String?,
        val Price: Double,
        val Price1: Double,
        val Price2: Double,
        val ProductList: List<ProductItem>,
        val ProductTypeId: Int,
        val Quantity: Int,
        val Sku: String,
        val UnitType: Int,
        val Url: String,
//        val Variant: Any,
        val VariantsGroup: VariantsGroup,
        val _id: String
    ) : Parcelable
}