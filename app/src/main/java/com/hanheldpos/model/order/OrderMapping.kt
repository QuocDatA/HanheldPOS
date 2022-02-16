package com.hanheldpos.model.order

import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.model.cart.ModifierCart
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.product.ProductType

abstract class OrderMapping {

    protected fun iMapperFee(src: Fee, subtotal: Double, totalDiscounts: Double): OrderFee {
        return OrderFee(
            _id = src._id,
            FeeType = src.Id,
            FeeName = src.Name,
            FeeValue = src.Value,
            TotalPrice = src.price(subtotal, totalDiscounts)
        );
    }

    /*protected fun iMapperDiscount(src: DiscountServer,proSubtotal : Double? , modSubtotal : Double?, productOriginal_id : String, quantity : Int ) : DiscountOrder{

    }*/

    protected fun iMapperDiscount(
        src: DiscountUser,
        proSubtotal: Double,
        modSubtotal: Double,
        productOriginal_id: String?,
        quantity: Int
    ): DiscountOrder {
        val proModSubtotal = proSubtotal + modSubtotal;

        return DiscountOrder(
            _id = "",
            DiscountName = src.DiscountName,
            DiscountType = src.DiscountType,
            DiscountValue = src.DiscountValue,
            ParentTypeId = src.DiscountType,
            DiscountQuantity = 1,
            DiscountTotalPrice = src.total(proModSubtotal),
            DiscountLineTotalPrice = src.total(proModSubtotal),
        );
    }

    protected fun iMapperCompVoid(src: Reason, parent_id: Int?, totalPrice: Double?): CompVoid {
        return CompVoid(
            CompVoidGuid = src.CompVoidGuid,
            CompVoidGroupId = parent_id,
            CompVoidTitle = src.Title,
            CompVoidValue = src.CompVoidValue,
            CompVoidTotalPrice = totalPrice
        )
    }

    protected fun iMapperModifier(
        src: ModifierCart,
        pricingMethodId: Int?,
        pricingValue: Double?,
        pricingPrice: Double?
    ): OrderModifier {

        return OrderModifier(
            ModifierItemGuid = src.modifierGuid,
            Name = src.name,
            ModifierQuantity = src.quantity,
            PricingMethodId = pricingMethodId,
            DiscountValue = pricingValue,
            PriceOriginal = src.price,
            Price = pricingPrice,
            ModifierSubtotal = pricingPrice?.times(src.quantity),
            DiscountTotalPrice = src.total(pricingPrice ?: 0.0)
        );
    }

    protected fun iMapperProductBuy(
        src: BaseProductInCart,
        indexOfList: Int,
        name1: String,
        name2: String,
        id: String,
        price: Double?,
        quantity: Int,
        totalDiscount: Double?,
        totalService: Double?,
        totalSurcharge: Double?,
        totalTax: Double?,
        totalModifier: Double?,
        pricingMethodType: Int?,
        subtotal: Double?,
        totalPrice: Double?,
        lineTotal: Double?,
        grossPrice: Double?,
        productType: ProductType,
        url: String?,
        modifierList: List<OrderModifier>?,
        compVoidList: List<CompVoid>?,
        discList: List<DiscountOrder>?,
        serviceList: List<OrderFee>?,
        surchargeList: List<OrderFee>?,
        taxesList: List<OrderFee>?,
        parentName: String?,
        parent_id: String?,
        totalFee: Double?,
        proModSubtotal: Double?,
        modSubtotal: Double?
    ): ProductBuy {

        return ProductBuy(
            OrderDetailId = indexOfList,
            _id = id,
            Name1 = name1,
            Name2 = name2,
            Sku = src.sku,
            Price = price,
            Quantity = quantity,
            Note = src.note,

            DiningOption = if (src.diningOption == null) null else OrderDiningOption(
                Id = src.diningOption?.Id ?: 0,
                TypeId = src.diningOption?.TypeId ?: 0,
                Title = src.diningOption?.Title,
                Acronymn = src.diningOption?.Acronymn
            ),

            VariantList = src.variantList,
            DiscountTotalPrice = totalDiscount,
            ServiceTotalPrice = totalService,
            SurchargeTotalPrice = totalSurcharge,
            TaxTotalPrice = totalTax,
            ModifierTotalPrice = totalModifier,
            PricingMethodType = pricingMethodType,
            Subtotal = subtotal,
            ModSubtotal = modSubtotal,
            ProdModSubtotal = proModSubtotal,
            LineTotal = lineTotal,
            GrossPrice = grossPrice,
            ProductTypeId = productType.value,
            Variant = src.variants,
            Url = url,
            ModifierList = modifierList,
            CompVoidList = compVoidList,
            DiscountList = discList,
            ServiceFeeList = serviceList,
            SurchargeFeeList = surchargeList,
            TaxFeeList = taxesList,
            OtherFee = totalFee,
            Category_id = src.proOriginal!!.CategoryGuid,
            Parent_id = parent_id,
            ParentName = parentName
        );
    }
}