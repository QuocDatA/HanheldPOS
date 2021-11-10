package com.hanheldpos.model.cart

import android.util.Log
import com.hanheldpos.data.api.pojo.product.ProductItem
import com.hanheldpos.model.product.ModPricingType
import com.hanheldpos.model.product.updateModifierPrice

data class ModifierCart(
    var modifierGroupName: String,
    var modifierGuid: String,
    var name: String,
    var quantity: Int,
    var price: Double?
) {
    fun total(pricingPrice: Double): Double? {
        var discountValue = price?.minus(pricingPrice);
        discountValue ?: return null;
        discountValue = if (discountValue < 0.0) 0.0 else discountValue;
        return discountValue;
    }

    fun subTotal(productPricing: ProductItem): Double {
        val subtotal = pricing(productPricing) * quantity;
        return subtotal;
    }

    fun pricing(productPricing: ProductItem): Double {
        val pricingType = productPricing.modifierPricingType ?: -1;
        val pricingValue = productPricing.modifierPricingValue ?: 0.0;
        when (ModPricingType.fromInt(pricingType)) {
            ModPricingType.FIX_AMOUNT -> {
                return pricingValue;
            }
            ModPricingType.USED_DEFAULT_PRICE -> {
                return price ?: 0.0;
            }
            ModPricingType.DISCOUNT_AMOUNT -> {
                val pricingPrice = (price ?: 0.0).minus(pricingValue);
                return if (pricingPrice < 0) 0.0 else pricingPrice;
            }
            ModPricingType.DISCOUNT_PERCENT -> {
                val pricingPrice = (price ?: 0.0).minus((price ?: 0.0) * pricingValue / 100);
                return pricingPrice;
            }
            ModPricingType.NONE -> {
                 return 0.0;
            }
            else -> {
                return price ?: 0.0;
            }
        }
    }
}