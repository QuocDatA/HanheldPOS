package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import com.hanheldpos.model.product.ModPricingType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModifierItem(
    val CreateDate: String,
    val Modifier: String,
    val ModifierGuid: String,
    val ModifierItemId: Int,
    val OrderNo: Int,
    val PreSelected: Boolean,
    val Price: Double,
    val PriceFormat: String?,
    val Url: String,
    val Visible: Int,
    val _Id: String,
    val _key: Int,
    val _rev: String
) : Parcelable {
    fun pricing(productPricing : Product) : Double{
        val pricingType = productPricing.ModifierPricingType ?: -1;
        val pricingValue = productPricing.ModifierPricingValue ?: 0.0;
        when (ModPricingType.fromInt(pricingType)) {
            ModPricingType.FIX_AMOUNT -> {
                return pricingValue;
            }
            ModPricingType.USED_DEFAULT_PRICE -> {
                return Price ?: 0.0;
            }
            ModPricingType.DISCOUNT_AMOUNT -> {
                val pricingPrice = (Price ?: 0.0).minus(pricingValue);
                return if (pricingPrice < 0) 0.0 else pricingPrice;
            }
            ModPricingType.DISCOUNT_PERCENT -> {
                val pricingPrice = (Price ?: 0.0).minus((Price ?: 0.0) * pricingValue / 100);
                return pricingPrice;
            }
            ModPricingType.NONE -> {
                return 0.0;
            }
            else -> {
                return Price ?: 0.0;
            }
        }
    }
}