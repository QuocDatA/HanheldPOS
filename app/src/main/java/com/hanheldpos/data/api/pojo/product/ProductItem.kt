package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.hanheldpos.model.home.order.ProductModeViewType
import com.hanheldpos.model.product.ProductComboItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ProductItem(

    @field:SerializedName("_id")
    val id: String,

    @field:SerializedName("_rev")
    val rev: String,

    @field:SerializedName("_key")
    val key: Int,

    @field:SerializedName("Acronymn")
    val acronymn: String? = null,

    @field:SerializedName("ProductId")
    val productID: Int,

    @field:SerializedName("Detail")
    val detail: @RawValue Any? = null,

    @field:SerializedName("CategoryGuid")
    val categoryGUID: String,

    @field:SerializedName("Name")
    val name: String,

    @field:SerializedName("Name1")
    val name1: String,

    @field:SerializedName("Description")
    val description: String,

    @field:SerializedName("Name2")
    val name2: String,

    @field:SerializedName("Description2")
    val description2: String,

    @field:SerializedName("Name3")
    val name3: String,

    @field:SerializedName("Name4")
    val name4: String,

    @field:SerializedName("AsignTo")
    val asignTo: Int,

    @field:SerializedName("Location")
    val location: String,

    @field:SerializedName("UnitType")
    val unitType: Int,

    @field:SerializedName("Price")
    val price: Double,

    @field:SerializedName("ComparePrice")
    val comparePrice: Double,

    @field:SerializedName("CostPerItem")
    val costPerItem: Double,

    @field:SerializedName("ChargeTaxes")
    val chargeTaxes: Int,

    @field:SerializedName("SKU")
    val sku: String,

    @field:SerializedName("Quantity")
    val quantity: Int,

    @field:SerializedName("InventoryPolicy")
    val inventoryPolicy: Int,

    @field:SerializedName("Barcode")
    val barcode: String,

    @field:SerializedName("StatusId")
    val statusID: Int,

    @field:SerializedName("Visible")
    val visible: Int,

    @field:SerializedName("Purchase")
    val purchase: Int,

    @field:SerializedName("CreateDate")
    val createDate: String,

    @field:SerializedName("Variants")
    val variants: String,

    @field:SerializedName("Modifier")
    val modifier: String,

    @field:SerializedName("Color")
    val color: String,

    @field:SerializedName("Combo")
    val combo: String,

    @field:SerializedName("PrintingLocation")
    val printingLocation: String,

    @field:SerializedName("PriceFormat")
    val priceFormat: String? = null,

    @field:SerializedName("ComparePriceFormat")
    val comparePriceFormat: String? = null,

    @field:SerializedName("Url")
    var url: String? = null,

    @field:SerializedName("Discount")
    val discount: String? = null,

    val strDiscount: String? = null,
    val strComparePrice: String? = null,
    val strSku: String? = null,
    val strName3: String? = null,
    val strUnit: String? = null,

    @field:SerializedName("Handle")
    val handle: String,

    @field:SerializedName("LocationLock")
    val locationLock: String? = null,

    @field:SerializedName("IsLock")
    val isLock: Boolean,

    @field:SerializedName("IsPriceFixed")
    val isPriceFixed: Boolean? = null,

    @field:SerializedName("PricingMethodType")
    val pricingMethodType: Int,

    @field:SerializedName("PricingMethodDiscount")
    val pricingMethodDiscount: Double,

    @field:SerializedName("ModifierPricingType")
    val modifierPricingType: Int,

    @field:SerializedName("ModifierPricingDescription")
    val modifierPricingDescription: String,

    @field:SerializedName("ModifierPricingValue")
    val modifierPricingValue: Double,

    @field:SerializedName("ProductPriceOverrideList")
    val productPriceOverrideList: List<ProductPriceOverrideListItem>?,

    @field:SerializedName("VariantPriceOverrideList")
    val variantPriceOverrideList: List<VariantPriceOverrideListItem>?,

    @field:SerializedName("VariantsGroup")
    val variantsGroup: VariantsGroup? = null,

    @field:SerializedName("VariantsGroupNameDefault")
    val variantsGroupNameDefault: String? = null,

    @field:SerializedName("VariantsGroupSkuDefault")
    val variantsGroupSkuDefault: String? = null,

    @field:SerializedName("ModifierPricing")
    val modifierPricing: String,

    @field:SerializedName("PricingMethod")
    val pricingMethod: String,

    @field:SerializedName("GroupPrices")
    val groupPrices: List<GroupPriceItem>? = null,

    @field:SerializedName("ProductTypeId")
    val productTypeID: String? = null,

    @field:SerializedName("ComboType")
    val comboType: Int,

    @field:SerializedName("Parent_id")
    val parentID: String? = null,

    ) : Parcelable, Cloneable {

    public override fun clone(): ProductItem {
        return copy()
    }

    fun isBundle() : Boolean {
        return groupComboList != null && groupComboList!!.any()
    }

    val groupComboList : List<ProductComboItem> get() = Gson().fromJson(combo, object : TypeToken<List<ProductComboItem>>() {}.type)

    val skuDefault get() = if (variantsGroup ==null) sku else variantsGroupSkuDefault

    val variantDefault get() = if (variantsGroup == null) "" else variantsGroupNameDefault

    fun priceOverride(locationId : String?,sku : String?, priceDefault : Double) : Double{
        if (locationId.isNullOrEmpty()) return price?: 0.0
        if (variantsGroup != null) {
            val priceOverride = variantPriceOverrideList?.firstOrNull { it.VariationSku.equals(sku) }
            val listPriceOverride : List<VariantsPriceOverrideLocation>? = Gson().fromJson(priceOverride?.PriceOverride, object : TypeToken<List<VariantsPriceOverrideLocation>>() {}.type)
            val priceOverrideOfLocation : VariantsPriceOverrideLocation? = listPriceOverride?.firstOrNull { it.LocationGuid.equals(locationId) }
            return if (priceOverrideOfLocation != null) priceOverrideOfLocation.Price ?: 0.0 else priceDefault
        }
        return productPriceOverrideList?.firstOrNull { it.LocationGuid.equals(locationId) }?.Price?: price
    }


}

@Parcelize
data class ProductPriceOverrideListItem(
    val LocationGuid : String?,
    val Price : Double?,
) : Parcelable

@Parcelize
data class VariantPriceOverrideListItem(
    val GroupName : String?,
    val VariationName : String?,
    val VariationSku : String?,
    val UnitTypeId : Int?,
    val Price : Double?,
    val PriceOverride : String?,
    val ProductGuid : String?,
) : Parcelable

@Parcelize
data class VariantsPriceOverrideLocation(
    val LocationGuid: String?,
    val Price: Double?,
) : Parcelable

@Parcelize
data class VariantsGroup(
    val DisplayName: String?,
    val IsDisplayName: Boolean?,
    val OptionName: String?,
    val OptionValueList: List<OptionValueVariantsGroup>? = null,
    val VariantOptionType: Int?
) : Parcelable {
    @Parcelize
    data class OptionValueVariantsGroup(
        val Barcode: String,
        val ComparePrice: Double,
        var OptionName: String?,
        val CostPerItem: Double,
        val Discount: Double,
        val GroupValue: String,
        val Id: Int,
        val Inventory: Int,
        val Level: Int,
        val Price: Double,
        val Sku: String,
        val Value: String,
        val Visible: Int,
        val Variant: VariantsGroup?,
    ) : Parcelable

    fun subOptionValueList() : List<OptionValueVariantsGroup>? {
        return OptionValueList?.map { option_value ->
            option_value.apply { this.OptionName = this@VariantsGroup.OptionName }
        }
    }
}