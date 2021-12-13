package com.hanheldpos.data.api.pojo.discount

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hanheldpos.data.api.pojo.product.ProductItem
import com.hanheldpos.model.product.ProductComboItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountResp(
    val DidError: Boolean,
    val ErrorMessage: String,
    val Message: String,
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
    val OnlyApplyDiscountOncePerOrder: Int,
    val OnlyApplyDiscountProductOncePerOrder: Int,
    val OrderNo: Int,
    val SetSchedules: Int,
//    val Trigger: List<Any>,
    val Url: String,
    val UsageLimits: String,
    val Watch: String,
    val _id: String,
    val jsaction: String
) : Parcelable {
    fun ListCustomerEligibility(): List<CustomerEligibility> =
        Gson().fromJson(this.ListCustomerEligibility,
            object : TypeToken<List<CustomerEligibility>>() {}.type);
}

data class CustomerEligibility(
    val Name: String,
    val _key: Int,
    val _id: String,
)


@Parcelize
data class Condition(
    val AppliesTo: Int,
    val CustomerBuys: CustomerBuys,
    val CustomerGets: CustomerGets,
    val DiscountGuid: String,
    val DiscountType: Int,
    val DiscountValue: Double,
    val IsDiscountLimit: Int,
    val IsMaxAmount: Int,
    val IsMaxQuantity: Int,
//    val ListApplyTo: Any,
    val ListFromItem: String,
    val MaximumDiscount: Double,
    val MaximumNumberOfUses: Int,
    val MaximumNumberOfUsesValue: Int,
    val _Id: String,
    val _key: Int,
    val _rev: String
) : Parcelable {
    fun listFromItem(): List<ProductDiscount> =
        Gson().fromJson(this.ListFromItem, object : TypeToken<List<ProductDiscount>>() {}.type);
}

@Parcelize
data class CustomerBuys(
    val ApplyTo: Int,
    val CustomerName: String,
    val Handle: String,
    val ListApplyTo: List<ProductItem>,
    val MinimumTypeId: Int,
    val MinimumValue: Double,
    val MinimumValueFormat: String,
    val ProductApplyTo: Int
) : Parcelable

@Parcelize
data class CustomerGets(
    val ApplyTo: Int,
    val CustomerName: String,
    val DiscountValue: Double,
    val DiscountValueType: Int,
    val Handle: String,
    val ListApplyTo: List<ProductItem>,
    val ProductApplyTo: Int,
    val Quantity: Int
) : Parcelable

@Parcelize
data class DiningOptionDiscount(
    val Id: Int,
    val Title : String
) : Parcelable

@Parcelize
data class DiscountsApplyToItem(
    val ApplyTo: Int,
    val DiscountAutomatic: Boolean,
    val DiscountGuid: String,
    val DiscountType: Int,
//    val ListCombo: Any,
    val ListParents: String,
    val ListProducts: String,
    val UserGuid: String
) : Parcelable {
    fun listProducts(): List<ProductDiscount> =
        Gson().fromJson(this.ListProducts, object : TypeToken<List<ProductDiscount>>() {}.type);
}

@Parcelize
data class VariantDiscount(
    val GroupId: String,
    val GroupName: String,
    val OrderNo: Int,
) : Parcelable

@Parcelize
data class ProductDiscount(
    val ProductGuid: String,
    val Name: String,
    val Sku: String,
    val TotalVariant: Int,
    val TotalVariantChecked: Int,
    val Url: String,
    val IsMaxAmount: Int,
    val IsMaxQuantity: Int,
    val MaxQuantity: Int,
    val MaxAmount: Double,
    val ApplyToModifier: Int,
    val ListVariant: List<VariantDiscount>
) : Parcelable {

}

