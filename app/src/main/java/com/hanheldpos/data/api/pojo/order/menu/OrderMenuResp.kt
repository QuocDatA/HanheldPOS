package com.hanheldpos.data.api.pojo.order.menu

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hanheldpos.data.api.pojo.product.GroupPriceItem
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class OrderMenuResp(

    @field:SerializedName("Message")
    val message: String? = null,

    @field:SerializedName("Model")
    val model: List<ModelItem?>? = null,

    @field:SerializedName("ErrorMessage")
    val errorMessage: String? = null,

    @field:SerializedName("DidError")
    val didError: Boolean? = null
) : Parcelable

@Parcelize
data class ProductItem(

    @field:SerializedName("_id")
    val id: String,

    @field:SerializedName("_rev")
    val rev: String,

    @field:SerializedName("_key")
    val key: Int,

    @field:SerializedName("ProductId")
    val productID: Int,

    @field:SerializedName("Detail")
    val detail:@RawValue Any? = null,

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
    val url: String? = null,

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

//    @field:SerializedName("ProductPriceOverrideList")
//    val productPriceOverrideList: List<Any?>,
//
//    @field:SerializedName("VariantPriceOverrideList")
//    val variantPriceOverrideList: List<Any?>,

    @field:SerializedName("VariantsGroup")
    val variantsGroup:@RawValue Any? = null,

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

    ) : Parcelable,Cloneable {

    public override fun clone(): ProductItem {
        return copy()
    }
}

@Parcelize
data class ListToHierarchyItem(

    @field:SerializedName("MenusType")
    val menusType: Int? = null,

    @field:SerializedName("MenusGroupGuid")
    val menusGroupGuid: String? = null,

    @field:SerializedName("Color")
    val color: String? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("Title")
    val title: String? = null,

    @field:SerializedName("MenusGuid")
    val menusGuid: String? = null,

    @field:SerializedName("Level")
    val level: Int? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("Children")
    val children: List<ListToHierarchyItem?>? = null,

    @field:SerializedName("Url")
    val url: String? = null,

    @field:SerializedName("GroupGuid")
    val groupGuid: String? = null
) : Parcelable

@Parcelize
data class CategoryItem(

    @field:SerializedName("CategoryId")
    val categoryId: Int? = null,

    @field:SerializedName("Description")
    val description: String? = null,

    @field:SerializedName("Color")
    val color: String? = null,

    @field:SerializedName("_rev")
    val rev: String? = null,

    @field:SerializedName("Title")
    val title: String? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("Guid")
    val guid: String? = null,

    @field:SerializedName("_key")
    val key: Int? = null,

    @field:SerializedName("CreateDate")
    val createDate: String? = null,

    @field:SerializedName("Handle")
    val handle: String? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("_Id")
    val id: String? = null,

    @field:SerializedName("Ansi")
    val ansi: String? = null
) : Parcelable {

}
@Parcelize
data class ProductModifierItem(

    @field:SerializedName("ModifierGuid")
    val modifierGuid: String? = null,

    @field:SerializedName("ProductGuid")
    val productGuid: String? = null,

    @field:SerializedName("RequiredModifier")
    val requiredModifier: Int? = null,

    @field:SerializedName("HideModifier")
    val hideModifier: Int? = null,

    @field:SerializedName("_rev")
    val rev: String? = null,

    @field:SerializedName("MaximumModifier")
    val maximumModifier: Int? = null,

    @field:SerializedName("ModifierItem")
    val modifierItem: String? = null,

    @field:SerializedName("_Id")
    val id: String? = null,

    @field:SerializedName("_key")
    val key: Int? = null,

    @field:SerializedName("CreateDate")
    val createDate: String? = null
) : Parcelable

@Parcelize
data class ProductImagesItem(

    @field:SerializedName("Type")
    val type: String? = null,

    @field:SerializedName("ProductGuid")
    val productGuid: String? = null,

    @field:SerializedName("Size")
    val size: Int? = null,

    @field:SerializedName("_rev")
    val rev: String? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("_Id")
    val id: String? = null,

    @field:SerializedName("_key")
    val key: Int? = null,

    @field:SerializedName("Url")
    val url: String? = null,

    @field:SerializedName("Name")
    val name: String? = null
) : Parcelable

@Parcelize
data class MenusItem(

    @field:SerializedName("AvailableFutureLocation")
    val availableFutureLocation: Int? = null,

    @field:SerializedName("Acronymn")
    val acronymn: String? = null,

    @field:SerializedName("UserGuid")
    val userGuid: String? = null,

    @field:SerializedName("_rev")
    val rev: String? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("_key")
    val key: Int? = null,

    @field:SerializedName("Name")
    val name: String? = null,

    @field:SerializedName("Handle")
    val handle: String? = null,

    @field:SerializedName("AsignTo")
    val asignTo: Int? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("_Id")
    val id: String? = null,

    @field:SerializedName("Ansi")
    val ansi: String? = null,

    @field:SerializedName("Location")
    val location: String? = null
) : Parcelable

@Parcelize
data class UnitsItem(

    @field:SerializedName("SystemUnitGroupId")
    val systemUnitGroupId: Int? = null,

    @field:SerializedName("Abbreviation")
    val abbreviation: String? = null,

    @field:SerializedName("SystemPrecisionName")
    val systemPrecisionName: String? = null,

    @field:SerializedName("SystemUnitId")
    val systemUnitId: Int? = null,

    @field:SerializedName("UserGuid")
    val userGuid: String? = null,

    @field:SerializedName("_rev")
    val rev: String? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("SystemUnitName")
    val systemUnitName: String? = null,

    @field:SerializedName("_Id")
    val id: String? = null,

    @field:SerializedName("_key")
    val key: Int? = null
) : Parcelable

@Parcelize
data class MenusGroupItem(

    @field:SerializedName("MenusType")
    val menusType: Int? = null,

    @field:SerializedName("UserGuid")
    val userGuid: String? = null,

    @field:SerializedName("_rev")
    val rev: String? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("MenusGuid")
    val menusGuid: String? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("_Id")
    val id: String? = null,

    @field:SerializedName("_key")
    val key: Int? = null,

    @field:SerializedName("CreateDate")
    val createDate: String? = null,

    @field:SerializedName("GroupGuid")
    val groupGuid: String? = null
) : Parcelable

@Parcelize
data class ModifierItemItem(

    @field:SerializedName("ModifierGuid")
    val modifierGuid: String? = null,

    @field:SerializedName("Price")
    val price: Double? = null,

    @field:SerializedName("_rev")
    val rev: String? = null,

    @field:SerializedName("ModifierItemId")
    val modifierItemId: Int? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("_Id")
    val id: String? = null,

    @field:SerializedName("_key")
    val key: Int? = null,

    @field:SerializedName("Modifier")
    val modifier: String? = null,

    @field:SerializedName("CreateDate")
    val createDate: String? = null
) : Parcelable

@Parcelize
data class MenusGroupShowInItem(

    @field:SerializedName("MenusType")
    val menusType: Int? = null,

    @field:SerializedName("_rev")
    val rev: String? = null,

    @field:SerializedName("ShowInGroupGuid")
    val showInGroupGuid: String? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("MenusGuid")
    val menusGuid: String? = null,

    @field:SerializedName("CategoryGuid")
    val categoryGuid: String? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("_Id")
    val id: String? = null,

    @field:SerializedName("_key")
    val key: Int? = null,

    @field:SerializedName("CreateDate")
    val createDate: String? = null,

    @field:SerializedName("GroupGuid")
    val groupGuid: String? = null
) : Parcelable

@Parcelize
data class MenusGroupItemItem(

    @field:SerializedName("UserGuid")
    val userGuid: String? = null,

    @field:SerializedName("_rev")
    val rev: String? = null,

    @field:SerializedName("ItemGuid")
    val itemGuid: String? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("MenusGuid")
    val menusGuid: String? = null,

    @field:SerializedName("IsSelected")
    val isSelected: Int? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("_Id")
    val id: String? = null,

    @field:SerializedName("_key")
    val key: Int? = null,

    @field:SerializedName("CreateDate")
    val createDate: String? = null,

    @field:SerializedName("GroupGuid")
    val groupGuid: String? = null
) : Parcelable

@Parcelize
data class CategoryImagesItem(

    @field:SerializedName("Type")
    val type: String? = null,

    @field:SerializedName("Size")
    val size: Int? = null,

    @field:SerializedName("_rev")
    val rev: String? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("CategoryGuid")
    val categoryGuid: String? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("_Id")
    val id: String? = null,

    @field:SerializedName("_key")
    val key: Int? = null,

    @field:SerializedName("Url")
    val url: String? = null,

    @field:SerializedName("Name")
    val name: String? = null,

    @field:SerializedName("StyleId")
    val styleId: Int? = null
) : Parcelable

@Parcelize
data class GroupsImagesItem(

    @field:SerializedName("Type")
    val type: String? = null,

    @field:SerializedName("Size")
    val size: Int? = null,

    @field:SerializedName("_rev")
    val rev: String? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("_Id")
    val id: String? = null,

    @field:SerializedName("_key")
    val key: Int? = null,

    @field:SerializedName("Url")
    val url: String? = null,

    @field:SerializedName("GroupGuid")
    val groupGuid: String? = null,

    @field:SerializedName("Name")
    val name: String? = null,

    @field:SerializedName("StyleId")
    val styleId: Int? = null
) : Parcelable

@Parcelize
data class GroupsItem(

    @field:SerializedName("GroupName")
    val groupName: String? = null,

    @field:SerializedName("MenusType")
    val menusType: Int? = null,

    @field:SerializedName("UserGuid")
    val userGuid: String? = null,

    @field:SerializedName("Color")
    val color: String? = null,

    @field:SerializedName("_rev")
    val rev: String? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("_Id")
    val id: String? = null,

    @field:SerializedName("_key")
    val key: Int? = null,

    @field:SerializedName("CreateDate")
    val createDate: String? = null,

    @field:SerializedName("Url")
    val url: String? = null,

    @field:SerializedName("Handle")
    val handle: String? = null
) : Parcelable {}

@Parcelize
data class ListMenusTypeItem(

    @field:SerializedName("Type")
    val type: Int? = null,

    @field:SerializedName("Title_en")
    val titleEn: String? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("Id")
    val id: Int? = null,

    @field:SerializedName("Title_vi")
    val titleVi: String? = null
) : Parcelable

@Parcelize
data class ModelItem(

    @field:SerializedName("ListProduct")
    val listProduct: List<ListProductItem?>? = null,

    @field:SerializedName("ListMenusGroupItemTree")
    val listMenusGroupItemTree: ListMenusGroupItemTree? = null,

    @field:SerializedName("MenusGroupShowIn")
    val menusGroupShowIn: List<MenusGroupShowInItem?>? = null,

    @field:SerializedName("Category")
    val category: List<CategoryItem?>? = null,

    @field:SerializedName("ListMenusType")
    val listMenusType: List<ListMenusTypeItem?>? = null,

    @field:SerializedName("domain_images")
    val domainImages: String? = null,

    @field:SerializedName("MenusGroupItem")
    val menusGroupItem: List<MenusGroupItemItem?>? = null,

    @field:SerializedName("Groups")
    val groups: List<GroupsItem?>? = null,

    @field:SerializedName("GroupsImages")
    val groupsImages: List<GroupsImagesItem?>? = null,

    @field:SerializedName("MenusGroup")
    val menusGroup: List<MenusGroupItem?>? = null,

    @field:SerializedName("Menus")
    val menus: List<MenusItem?>? = null,

    @field:SerializedName("CategoryImages")
    val categoryImages: List<CategoryImagesItem?>? = null
) : Parcelable

@Parcelize
data class ListToArrayItem(

    @field:SerializedName("MenusType")
    val menusType: Int? = null,

    @field:SerializedName("MenusGroupGuid")
    val menusGroupGuid: String? = null,

    @field:SerializedName("Color")
    val color: String? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("Title")
    val title: String? = null,

    @field:SerializedName("MenusGuid")
    val menusGuid: String? = null,

    @field:SerializedName("Level")
    val level: Int? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("Url")
    val url: String? = null,

    @field:SerializedName("GroupGuid")
    val groupGuid: String? = null,

    @field:SerializedName("ShowInGroupGuid")
    val showInGroupGuid: String? = null
) : Parcelable

//@Parcelize
//data class ChildrenItem(
//
//	@field:SerializedName("MenusType")
//	val menusType: Int? = null,
//
//	@field:SerializedName("MenusGroupGuid")
//	val menusGroupGuid: String? = null,
//
//	@field:SerializedName("Color")
//	val color: String? = null,
//
//	@field:SerializedName("Visible")
//	val visible: Int? = null,
//
//	@field:SerializedName("Title")
//	val title: String? = null,
//
//	@field:SerializedName("MenusGuid")
//	val menusGuid: String? = null,
//
//	@field:SerializedName("Level")
//	val level: Int? = null,
//
//	@field:SerializedName("OrderNo")
//	val orderNo: Int? = null,
//
//	@field:SerializedName("Children")
//	val children: List<Any?>? = null,
//
//	@field:SerializedName("Url")
//	val url: String? = null,
//
//	@field:SerializedName("GroupGuid")
//	val groupGuid: String? = null
//) : Parcelable

@Parcelize
data class ListProductItem(

    @field:SerializedName("ProductImages")
    val productImages: List<ProductImagesItem?>? = null,

    @field:SerializedName("ProductModifier")
    val productModifier: List<ProductModifierItem?>? = null,

    @field:SerializedName("Product")
    val product: List<ProductItem?>? = null,

    @field:SerializedName("ModifierItem")
    val modifierItem: List<ModifierItemItem?>? = null,

    @field:SerializedName("Units")
    val units: List<UnitsItem?>? = null
) : Parcelable

@Parcelize
data class ListMenusGroupItemTree(

    @field:SerializedName("ListToHierarchy")
    val listToHierarchy: List<ListToHierarchyItem?>? = null,

    @field:SerializedName("ListToArray")
    val listToArray: List<ListToArrayItem?>? = null
) : Parcelable
