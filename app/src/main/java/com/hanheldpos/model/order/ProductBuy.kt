package com.hanheldpos.model.order

import android.os.Parcelable
import com.hanheldpos.model.cart.VariantCart
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductBuy(
    val _id: String,
    val Name1: String,
    val Name2: String,
    val Sku: String?,
    val Note: String?,
    val Variant: String?,
    val Url: String?,
    val Price: Double?,
    val ModifierTotalPrice : Double?,
    val ModSubtotal : Double?,
    val ProdModSubtotal : Double?,
    val Quantity: Int,
    val Subtotal: Double?,
    val DiscountTotalPrice: Double?,
    val ServiceTotalPrice : Double?,
    val SurchargeTotalPrice: Double?,
    val TaxTotalPrice : Double?,
    val OtherFee : Double?,
    val LineTotal : Double?,
    val GrossPrice: Double?,
    val DiningOption : OrderDiningOption?,
    val VariantList : List<VariantCart>?,
    var ProductChoosedList : List<ProductBuy>? = null,
    val BuyXGetY : BuyXGetYEntire? = null,
    val ModifierList : List<OrderModifier>?,
    val DiscountList : List<DiscountOrder>?,
    val ServiceFeeList : List<OrderFee>?,
    val SurchargeFeeList : List<OrderFee>?,
    val ShippingFeeList : List<OrderFee>? = null,
    val TaxFeeList : List<OrderFee>?,
    val CompVoidList :  List<CompVoid>?,
    val OrderDetailId : Int,
    val ProductTypeId: Int,
    val PricingMethodType: Int?,
    val Category_id: String,
    val Group_id : String? = null,
    val Parent_id : String?,
    val ParentName : String?,
    val ProductApplyTo : Int = ChooseProductApplyTo.DEFAULT.value
    ) : Parcelable {
}