package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hanheldpos.data.api.pojo.order.menu.DateRange
import com.hanheldpos.data.api.pojo.order.menu.Schedules
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.product.ProductComboItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val Acronymn: String,
    val AsignTo: Int,
    val Barcode: String,
    val BrandGuid: String,
    val CategoryGuid: String,
    val ChargeTaxes: Int,
    val Color: String,
    val Combo: String,
    val ComboType: Int,
    val ComparePrice: String?,
    val ComparePriceFormat: String?,
    val CostPerItem: Double,
    val CreateDate: String,
    val Description: String,
    val Description2: String,
//    val Detail: Any,
    val Discount: String?,
    val GroupPrices: List<GroupPriceItem>? = null,
    val Handle: String,
    val InventoryPolicy: Int,
    val IsLock: Boolean,
    val IsPriceFixed: Boolean,
    val ListDateRange: List<DateRange>,
    val ListSchedules: List<Schedules>,
    val Location: String,
    val LocationLock: String,
    val Modifier: String,
    val ModifierPricing: String,
    val ModifierPricingDescription: String,
    val ModifierPricingType: Int,
    val ModifierPricingValue: Double,
    val Name: String,
    val Name1: String,
    val Name2: String,
    val Name3: String,
    val Name4: String,
    val Parent_id: String?,
    val Price: Double,
    val PriceFormat: String?,
    val PricingMethod: String,
    val PricingMethodDiscount: Double,
    val PricingMethodType: Int,
    val PrintingLocation: String,
    val ProductId: Int,
    val ProductModifiersList: List<ProductModifiers>,
    val ProductPriceOverrideList: List<ProductPriceOverride>? = null,
    val ProductType: Int,
    val ProductTypeId: String?,
    val Purchase: Int,
    val Quantity: Int,
    val SKU: String,
    val StatusId: Int,
    val UnitType: Int,
    val Units: List<UnitsItem>? = null,
    var Url: String,
    val UrlList: List<UrlListItem>,
    val VariantPriceOverrideList: List<VariantPriceOverrideListItem>?= null,
    val Variants: String,
    val VariantsGroup: VariantsGroup? = null,
    val VariantsGroupNameDefault: String?,
    val VariantsGroupSkuDefault: String?,
    val Visible: Int,
    val _id: String,
    val _key: Int,
    val _rev: String,
    val isDateRange: Int,
    val isSchedules: Int,
    val strComparePrice: String,
    val strDiscount: String,
    val strName3: String,
    val strSku: String,
    val strUnit: String
) : Parcelable, Cloneable {

    @Parcelize
    data class UrlListItem(
        val Url : String,
    ) : Parcelable

    public override fun clone(): Product {
        return copy()
    }

    fun isBundle(): Boolean {
        return groupComboList.any()
    }

    val groupComboList: List<ProductComboItem>
        get() = Gson().fromJson(
            Combo,
            object : TypeToken<List<ProductComboItem>>() {}.type
        )

    val skuDefault get() = if (VariantsGroup == null) SKU else VariantsGroupSkuDefault

    val variantDefault get() = if (VariantsGroup == null) "" else VariantsGroupNameDefault

    fun priceOverride(locationId: String?, sku: String?, priceDefault: Double): Double {
        val locationIdLast = locationId ?: UserHelper.getLocationGuid()
        if (VariantsGroup != null) {
            val priceOverride =
                VariantPriceOverrideList?.firstOrNull { it.VariationSku.equals(sku) }
            val listPriceOverride: List<VariantsPriceOverrideLocation>? = Gson().fromJson(
                priceOverride?.PriceOverride,
                object : TypeToken<List<VariantsPriceOverrideLocation>>() {}.type
            )
            val priceOverrideOfLocation: VariantsPriceOverrideLocation? =
                listPriceOverride?.firstOrNull { it.LocationGuid.equals(locationIdLast) }
            return if (priceOverrideOfLocation != null) priceOverrideOfLocation.Price
                ?: 0.0 else priceDefault
        }
        return ProductPriceOverrideList?.firstOrNull { it.LocationGuid == locationIdLast }?.Price
            ?: Price
    }
}

