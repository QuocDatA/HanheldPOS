package com.hanheldpos.model.order

import android.os.Parcelable
import android.util.Pair
import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import com.hanheldpos.model.cart.VariantCart
import com.hanheldpos.model.product.ProductType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductChosen(
    val _id: String?,
    val Name1: String?,
    val Name2: String?,
    val Sku: String?,
    val Note: String?,
    val Variant: String?,
    val Url: String?,
    val Price: Double?,
    val ModifierTotalPrice: Double?,
    val ModSubtotal: Double?,
    val ProdModSubtotal: Double?,
    val Quantity: Int,
    val Subtotal: Double?,
    val DiscountTotalPrice: Double?,
    val ServiceTotalPrice: Double?,
    val SurchargeTotalPrice: Double?,
    val TaxTotalPrice: Double?,
    val OtherFee: Double?,
    val LineTotal: Double?,
    val GrossPrice: Double?,
    val DiningOption: OrderDiningOption?,
    val VariantList: List<VariantCart>?,
    var ProductChoosedList: List<ProductChosen>? = null,
    val BuyXGetY: BuyXGetYEntire? = null,
    val ModifierList: List<OrderModifier>?,
    val DiscountList: List<DiscountOrder>?,
    val ServiceFeeList: List<OrderFee>?,
    val SurchargeFeeList: List<OrderFee>?,
    val ShippingFeeList: List<OrderFee>? = null,
    val TaxFeeList: List<OrderFee>?,
    val CompVoidList: List<CompVoid>?,
    val OrderDetailId: Int,
    val ProductTypeId: Int,
    val PricingMethodType: Int?,
    val Category_id: String?,
    val Group_id: String? = null,
    val Parent_id: String?,
    val ParentName: String?,
    val ParentIndex: Int,
    var ProductApplyTo: Int = ChooseProductApplyTo.DEFAULT.value
) : Parcelable {

    constructor(
        orderDetailId: Int,
        productTypeId: Int,
        parentName: String?,
        productApplyTo: Int? = 0,
        buyXGetY: BuyXGetYEntire,
    ) : this(
        OrderDetailId = orderDetailId,
        ProductTypeId = productTypeId,
        ParentName = parentName,
        ProductApplyTo = productApplyTo!!,
        BuyXGetY = buyXGetY,
        _id = null,
        Name1 = null,
        Name2 = null,
        Sku = null,
        Note = null,
        Variant = null,
        Url = null,
        Price = 0.0,
        ModifierTotalPrice = 0.0,
        ModSubtotal = 0.0,
        ProdModSubtotal = 0.0,
        Quantity = 0,
        Subtotal = 0.0,
        DiscountTotalPrice = 0.0,
        ServiceTotalPrice = 0.0,
        SurchargeTotalPrice = 0.0,
        TaxTotalPrice = 0.0,
        OtherFee = 0.0,
        LineTotal = 0.0,
        GrossPrice = 0.0,
        DiningOption = null,
        VariantList = null,
        ProductChoosedList = null,
        ModifierList = null,
        DiscountList = null,
        ServiceFeeList = null,
        SurchargeFeeList = null,
        ShippingFeeList = null,
        TaxFeeList = null,
        CompVoidList = null,
        PricingMethodType = 0,
        Category_id = null,
        Group_id = null,
        Parent_id = null,
        ParentIndex = 0,
    )

    constructor(
        orderDetailId: Int,
        name1: String? = null,
        _id: String? = null,
        quantity: Int? = 0,
        subtotal: Double? = 0.0,
        lineTotal: Double? = 0.0,
        grossPrice: Double? = 0.0,
        productTypeId: Int,
        compVoidList: List<CompVoid>?,
        proModSubTotal: Double?,
        note: String?,
        diningOption: DiningOption?,
        productChosenList: List<ProductChosen>,
    ) : this(
        OrderDetailId = orderDetailId,
        ProductTypeId = productTypeId,
        ParentName = null,
        ProductApplyTo = 0,
        BuyXGetY = null,
        _id = _id,
        Name1 = name1,
        Name2 = null,
        Sku = null,
        Note = note,
        Variant = null,
        Url = null,
        Price = 0.0,
        ModifierTotalPrice = 0.0,
        ModSubtotal = 0.0,
        ProdModSubtotal = proModSubTotal,
        Quantity = quantity!!,
        Subtotal = subtotal,
        DiscountTotalPrice = 0.0,
        ServiceTotalPrice = 0.0,
        SurchargeTotalPrice = 0.0,
        TaxTotalPrice = 0.0,
        OtherFee = 0.0,
        LineTotal = lineTotal,
        GrossPrice = grossPrice,
        DiningOption = OrderDiningOption(
            Id = diningOption?.Id ?: 0,
            TypeId = diningOption?.TypeId ?: 0,
            Title = diningOption?.Title,
            Acronymn = diningOption?.Acronymn
        ),
        VariantList = null,
        ProductChoosedList = productChosenList,
        ModifierList = null,
        DiscountList = null,
        ServiceFeeList = null,
        SurchargeFeeList = null,
        ShippingFeeList = null,
        TaxFeeList = null,
        CompVoidList = compVoidList,
        PricingMethodType = 0,
        Category_id = null,
        Group_id = null,
        Parent_id = null,
        ParentIndex = 0,
    )


    fun groupProducts(): List<GroupProductChosen> {
        return ProductChoosedList?.groupBy { Pair(it.ParentIndex, (it.ParentName ?: "")) }?.toList()
            ?.map {
                GroupProductChosen(it.first.first, it.first.second, it.second)
            }
            ?: mutableListOf()
    }

}

