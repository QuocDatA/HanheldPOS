package com.hanheldpos.model.cart

import android.os.Parcelable
import android.util.Log
import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.product.ModPricingType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModifierCart(
    var modifierId : String,
    var modifierGuid: String,
    var name: String,
    var quantity: Int,
    var price: Double?
) : Parcelable {
    fun total(pricingPrice: Double): Double? {
        var discountValue = price?.minus(pricingPrice);
        discountValue ?: return null;
        discountValue = if (discountValue < 0.0) 0.0 else discountValue;
        return discountValue;
    }

    fun subTotal(productPricing: Product): Double {
        val subtotal = pricing(productPricing) * quantity;
        return subtotal;
    }

    fun pricing(productPricing: Product?): Double {
        val pricingType = productPricing?.ModifierPricingType ?: -1;
        val pricingValue = productPricing?.ModifierPricingValue ?: 0.0;
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