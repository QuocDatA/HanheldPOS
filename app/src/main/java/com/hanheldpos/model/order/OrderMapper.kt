package com.hanheldpos.model.order

import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.order.menu.getProductImage
import com.hanheldpos.data.api.pojo.order.settings.Reason
import com.hanheldpos.data.api.pojo.product.ProductItem
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
        var compVoids = mutableListOf<CompVoid>();
        reason ?: return compVoids;
        val parent_id = DataHelper.getVoidInfo()?.id;
        var compVoid = iMapperCompVoid(reason, parent_id, totalPrice);
        compVoids.add(compVoid);
        return compVoids;
    }

    fun mappingModifierList(
        modifierCarts: List<ModifierCart>,
        proOriginal: ProductItem
    ): List<OrderModifier> {
        val modifierOrders = mutableListOf<OrderModifier>()

        modifierCarts.forEach { modifierCart ->
            var pricingPrice = modifierCart.pricing(proOriginal);

            var modifier = iMapperModifier(
                modifierCart,
                proOriginal.pricingMethodType,
                proOriginal.modifierPricingValue,
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
        var modSubtotal = regular.modSubTotal(regular.proOriginal!!);
        var totalModifier = modSubtotal * regular.quantity!!;
        var proModSubtotal = regular.priceOverride!! + modSubtotal;
        var subtotal = regular.subTotal(regular.proOriginal!!);
        var lineTotal = regular.total(regular.proOriginal!!);
        var totalPrice = regular.totalPrice();

        var url = DataHelper.orderMenuResp?.getProductImage()?.firstOrNull { p ->
            p?.productGuid
                .equals(regular.proOriginal?.id)
        }?.url;

        var totalDiscount = regular.totalDiscount(regular.proOriginal!!);
        var totalCompVoid = regular.totalComp(regular.proOriginal!!);
        var totalService = regular.fees?.firstOrNull { fee -> fee.feeType == FeeType.ServiceFee }
            ?.price(subtotal, totalDiscount);
        var totalSurcharge =
            regular.fees?.firstOrNull { fee -> fee.feeType == FeeType.SurchargeFee }
                ?.price(subtotal, totalDiscount);
        var totalTax = regular.fees?.firstOrNull { fee -> fee.feeType == FeeType.TaxFee }
            ?.price(subtotal, totalDiscount);

        var totalFee = regular.totalFee(subtotal, totalDiscount);
        var grossPrice = regular.grossPrice(subtotal, totalFee);

        var modifierList = mappingModifierList(regular.modifierList, regular.proOriginal!!);
        var compVoidList = mappingCompVoidList(regular.compReason, totalCompVoid);
        var discountList = mappingDiscountList(
            regular.discountServersList,
            regular.discountUsersList,
            totalPrice,
            totalModifier,
            regular.proOriginal!!.id,
            regular.quantity
        );
        var surchargeFeeList = mappingFeeList(
            regular.fees ?: mutableListOf(),
            FeeType.SurchargeFee,
            subtotal,
            totalDiscount
        );
        var serviceFeeList = mappingFeeList(
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
            regular.proOriginal!!.name,
            regular.proOriginal!!.name3,
            regular.proOriginal!!.id,
            regular.priceOverride,
            quantity,
            totalDiscount,
            totalService,
            totalSurcharge,
            totalTax,
            totalModifier,
            regular.proOriginal!!.pricingMethodType,
            subtotal,
            totalPrice,
            lineTotal,
            grossPrice,
            ProductType.REGULAR,
            url,
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

    fun mappingSubProductBuyBundle(regular :Regular, proOriginalCombo :ProductItem, group : GroupBundle, index : Int,  orderDetailId : Int) :ProductBuy {
        var parentName = DataHelper.findGroupNameOrderMenu(group.comboInfo.comboGuid!!);
        var url = DataHelper.orderMenuResp?.getProductImage()?.firstOrNull { p ->
            p?.productGuid
                .equals(regular.proOriginal?.id)
        }?.url;

        var modSubtotal = regular.modSubTotal(proOriginalCombo);
        var groupPrice = regular.groupPrice(group, proOriginalCombo);
        var totalPrice = groupPrice * regular.quantity!!;
        var modifierList = mappingModifierList(regular.modifierList, proOriginalCombo);
        var totalModifier = modSubtotal * regular.quantity!!;
        var subtotal = totalPrice + totalModifier;
        var proModSubtotal = groupPrice + modSubtotal;

        return iMapperProductBuy(regular, orderDetailId, regular.proOriginal!!.name, regular.proOriginal!!.name3, regular.proOriginal!!.id, groupPrice, regular.quantity!!, 0.0,
            0.0, 0.0, 0.0, totalModifier, null, subtotal, totalPrice, subtotal, 0.0, ProductType.REGULAR,
            url, modifierList, null, null, null, null, null, parentName, group.comboInfo.comboGuid, 0.0, proModSubtotal, modSubtotal);
    }

    fun mappingProductBuy(bundle: Combo, indexOfList: Int, parentName: String? = null): ProductBuy {
        var proChooseList = mutableListOf<ProductBuy>();

        var totalModifier = bundle.totalModifier();
        var subtotal = bundle.subTotal();
        var lineTotal = bundle.total();

        var totalCompVoid = bundle.totalComp();
        var url = DataHelper.orderMenuResp?.getProductImage()?.firstOrNull { p ->
            p?.productGuid
                .equals(bundle.proOriginal?.id)
        }?.url;

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


        val totalProductChoosedList = proChooseList.sumOf { pro -> pro.LineTotal?: 0.0 };
        val proModSubtotal = bundle.priceOverride!! + totalProductChoosedList;
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

        val DiscountList = mappingDiscountList(
            bundle.discountServersList,
            bundle.discountUsersList,
            totalPrice ?: 0.0, totalModifier, bundle.proOriginal!!.id, bundle.quantity);
        val CompVoidList = mappingCompVoidList(bundle.compReason, totalCompVoid);
        val ServiceFeeList =
            mappingFeeList(bundle.fees?: mutableListOf(), FeeType.ServiceFee, subtotal, totalDiscount);
        val TaxesFeeList =
            mappingFeeList(bundle.fees?: mutableListOf(), FeeType.TaxFee, subtotal, totalDiscount);
        val SurchargeFeeList = mappingFeeList(
            bundle.fees?: mutableListOf(),
            FeeType.SurchargeFee,
            subtotal,
            totalDiscount
        );

        var proBuy = iMapperProductBuy(
            bundle,
            indexOfList,
            bundle.proOriginal!!.name,
            bundle.proOriginal!!.name3,
            bundle.proOriginal!!.id,
            bundle.priceOverride,
            bundle.quantity!!,
            totalDiscount,
            totalService,
            totalSurcharge,
            totalTax,
            totalModifier,
            bundle.proOriginal!!.pricingMethodType,
            subtotal,
            totalPrice,
            lineTotal,
            grossPrice,
            ProductType.BUNDLE,
            url,
            null,
            CompVoidList,
            DiscountList,
            ServiceFeeList,
            SurchargeFeeList,
            TaxesFeeList,
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