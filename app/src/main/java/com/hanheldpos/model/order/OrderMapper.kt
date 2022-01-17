package com.hanheldpos.model.order

import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.Combo
import com.hanheldpos.model.cart.GroupBundle
import com.hanheldpos.model.cart.ModifierCart
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.cart.fee.FeeType
import com.hanheldpos.model.discount.DiscountServer
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.model.product.ProductType

object OrderMapper : OrderMapping() {

    fun mappingFeeList(
        fees: List<Fee>,
        feeType: FeeType,
        subtotal: Double?,
        totalDiscounts: Double?
    ): List<OrderFee> {
        return fees.filter { f -> f.feeType == feeType }
            .map { fee -> iMapperFee(fee, subtotal ?: 0.0, totalDiscounts ?: 0.0) }
    }

    fun mappingDiscountList(
        discountServers: List<DiscountServer>?,
        discountUsers: List<DiscountUser>?,
        proSubtotal: Double,
        modSubtotal: Double,
        productOriginal_id: String? = null,
        quantity: Int? = 1
    ): List<DiscountOrder> {
        var discountOrders = mutableListOf<DiscountOrder>();

        // Mapping discount form client.
        val disUser = discountUsers?.map { disc ->
            iMapperDiscount(
                disc,
                proSubtotal,
                modSubtotal,
                productOriginal_id,
                quantity ?: 0
            )
        }
        if (disUser != null) {
            discountOrders.addAll(disUser);
        }
        return discountOrders;
    }

    fun mappingCompVoidList(reason: Reason?, totalPrice: Double?): List<CompVoid> {
        val compVoids = mutableListOf<CompVoid>();
        reason ?: return compVoids;
        val parentId = DataHelper.getVoidInfo()?.Id;
        val compVoid = iMapperCompVoid(reason, parentId, totalPrice);
        compVoids.add(compVoid);
        return compVoids;
    }

    fun mappingModifierList(
        modifierCarts: List<ModifierCart>,
        proOriginal: Product
    ): List<OrderModifier> {
        val modifierOrders = mutableListOf<OrderModifier>()

        modifierCarts.forEach { modifierCart ->
            var pricingPrice = modifierCart.pricing(proOriginal);

            var modifier = iMapperModifier(
                modifierCart,
                proOriginal.PricingMethodType,
                proOriginal.ModifierPricingValue,
                pricingPrice
            );
            modifierOrders.add(modifier);
        }
        return modifierOrders;
    }

    fun mappingProductBuy(
        regular: Regular,
        indexOfList: Int,
        quantity: Int,
        parentName: String? = null
    ): ProductBuy {
        val modSubtotal = regular.modSubTotal(regular.proOriginal!!);
        val totalModifier = modSubtotal * regular.quantity!!;
        val proModSubtotal = regular.priceOverride!! + modSubtotal;
        val subtotal = regular.subTotal(regular.proOriginal!!);
        val lineTotal = regular.total(regular.proOriginal!!);
        val totalPrice = regular.totalPrice();


        val totalDiscount = regular.totalDiscount(regular.proOriginal!!);
        val totalCompVoid = regular.totalComp(regular.proOriginal!!);
        val totalService = regular.fees?.firstOrNull { fee -> fee.feeType == FeeType.ServiceFee }
            ?.price(subtotal, totalDiscount);
        val totalSurcharge =
            regular.fees?.firstOrNull { fee -> fee.feeType == FeeType.SurchargeFee }
                ?.price(subtotal, totalDiscount);
        val totalTax = regular.fees?.firstOrNull { fee -> fee.feeType == FeeType.TaxFee }
            ?.price(subtotal, totalDiscount);

        val totalFee = regular.totalFee(subtotal, totalDiscount);
        val grossPrice = regular.grossPrice(subtotal, totalFee);

        val modifierList = mappingModifierList(regular.modifierList, regular.proOriginal!!);
        val compVoidList = mappingCompVoidList(regular.compReason, totalCompVoid);
        val discountList = mappingDiscountList(
            regular.discountServersList,
            regular.discountUsersList,
            totalPrice,
            totalModifier,
            regular.proOriginal!!._id,
            regular.quantity
        );
        val surchargeFeeList = mappingFeeList(
            regular.fees ?: mutableListOf(),
            FeeType.SurchargeFee,
            subtotal,
            totalDiscount
        );
        val serviceFeeList = mappingFeeList(
            regular.fees ?: mutableListOf(),
            FeeType.ServiceFee,
            subtotal,
            totalDiscount
        );
        var taxesFeeList = mappingFeeList(
            regular.fees ?: mutableListOf(),
            FeeType.TaxFee,
            subtotal,
            totalDiscount
        );

        return iMapperProductBuy(
            regular,
            indexOfList,
            regular.proOriginal!!.Name,
            regular.proOriginal!!.Name3,
            regular.proOriginal!!._id,
            regular.priceOverride,
            quantity,
            totalDiscount,
            totalService,
            totalSurcharge,
            totalTax,
            totalModifier,
            regular.proOriginal!!.PricingMethodType,
            subtotal,
            totalPrice,
            lineTotal,
            grossPrice,
            ProductType.REGULAR,
            null,
            modifierList,
            compVoidList,
            discountList,
            serviceFeeList,
            surchargeFeeList,
            taxesFeeList,
            parentName,
            null,
            totalFee,
            proModSubtotal,
            modSubtotal
        );
    }

    fun mappingSubProductBuyBundle(
        regular: Regular,
        proOriginalCombo: Product,
        group: GroupBundle,
        index: Int,
        orderDetailId: Int
    ): ProductBuy {
        val parentName = DataHelper.findGroupNameOrderMenu(group.comboInfo.ComboGuid!!);
        val url = null;
        val modSubtotal = regular.modSubTotal(proOriginalCombo);
        val groupPrice = regular.groupPrice(group, proOriginalCombo);
        val totalPrice = groupPrice * regular.quantity!!;
        val modifierList = mappingModifierList(regular.modifierList, proOriginalCombo);
        val totalModifier = modSubtotal * regular.quantity!!;
        val subtotal = totalPrice + totalModifier;
        val proModSubtotal = groupPrice + modSubtotal;

        return iMapperProductBuy(
            regular,
            orderDetailId,
            regular.proOriginal!!.Name,
            regular.proOriginal!!.Name3,
            regular.proOriginal!!._id,
            groupPrice,
            regular.quantity!!,
            0.0,
            0.0,
            0.0,
            0.0,
            totalModifier,
            null,
            subtotal,
            totalPrice,
            subtotal,
            0.0,
            ProductType.REGULAR,
            url,
            modifierList,
            null,
            null,
            null,
            null,
            null,
            parentName,
            group.comboInfo.ComboGuid,
            0.0,
            proModSubtotal,
            modSubtotal
        );
    }

    fun mappingProductBuy(bundle: Combo, indexOfList: Int, parentName: String? = null): ProductBuy {
        val proChooseList = mutableListOf<ProductBuy>();

        val totalModifier = bundle.totalModifier();
        val subtotal = bundle.subTotal();
        val lineTotal = bundle.total();

        val totalCompVoid = bundle.totalComp();
        val url = null;

        var orderDetailId = 0;
        bundle.groupList.forEachIndexed { index, groupBundle ->
            groupBundle.productList.forEachIndexed { indexChild, regular ->
                val proChoose = mappingSubProductBuyBundle(
                    regular,
                    bundle.proOriginal!!,
                    groupBundle,
                    index,
                    orderDetailId
                );
                proChooseList.add(proChoose);

                orderDetailId++;
            }
        }


        val totalProductChosenList = proChooseList.sumOf { pro -> pro.LineTotal ?: 0.0 };
        val proModSubtotal = bundle.priceOverride!! + totalProductChosenList;
        val totalPrice = proModSubtotal * bundle.quantity!!;

        val totalDiscount = bundle.totalDiscount();
        val totalService = bundle.fees?.firstOrNull { fee -> fee.feeType == FeeType.ServiceFee }
            ?.price(subtotal, totalDiscount);
        val totalSurcharge =
            bundle.fees?.firstOrNull { fee -> fee.feeType == FeeType.SurchargeFee }
                ?.price(subtotal, totalDiscount);
        val totalTax = bundle.fees?.firstOrNull { fee -> fee.feeType == FeeType.TaxFee }
            ?.price(subtotal, totalDiscount);
        val totalFee = bundle.totalFee(subtotal, totalDiscount);
        val grossPrice = bundle.grossPrice(subtotal, totalFee);

        val discountList = mappingDiscountList(
            bundle.discountServersList,
            bundle.discountUsersList,
            totalPrice ?: 0.0, totalModifier, bundle.proOriginal!!._id, bundle.quantity
        );
        val compVoidList = mappingCompVoidList(bundle.compReason, totalCompVoid);
        val serviceFeeList =
            mappingFeeList(
                bundle.fees ?: mutableListOf(),
                FeeType.ServiceFee,
                subtotal,
                totalDiscount
            );
        val taxesFeeList =
            mappingFeeList(bundle.fees ?: mutableListOf(), FeeType.TaxFee, subtotal, totalDiscount);
        val surchargeFeeList = mappingFeeList(
            bundle.fees ?: mutableListOf(),
            FeeType.SurchargeFee,
            subtotal,
            totalDiscount
        );

        val proBuy = iMapperProductBuy(
            bundle,
            indexOfList,
            bundle.proOriginal!!.Name,
            bundle.proOriginal!!.Name3,
            bundle.proOriginal!!._id,
            bundle.priceOverride,
            bundle.quantity!!,
            totalDiscount,
            totalService,
            totalSurcharge,
            totalTax,
            totalModifier,
            bundle.proOriginal!!.PricingMethodType,
            subtotal,
            totalPrice,
            lineTotal,
            grossPrice,
            ProductType.BUNDLE,
            url,
            null,
            compVoidList,
            discountList,
            serviceFeeList,
            surchargeFeeList,
            taxesFeeList,
            parentName,
            null,
            totalFee,
            proModSubtotal,
            0.0
        );

        proBuy.ProductChoosedList = proChooseList;
        return proBuy;
    }
}