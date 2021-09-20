package com.hanheldpos.data.api.pojo.order.menu

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VariantStrProduct(
    @field:SerializedName("Group")
    val group: List<GroupItem?>? = null,

    @field:SerializedName("Option")
    val option: List<OptionItem?>? = null
) : Parcelable

@Parcelize
data class GroupItem(

    @field:SerializedName("GroupName")
    val groupName: String? = null,

    @field:SerializedName("Price")
    val price: Double? = null,

    @field:SerializedName("Barcode")
    val barcode: String? = null,

    @field:SerializedName("ComparePrice")
    val comparePrice: Double? = null,

    @field:SerializedName("Visible")
    val visible: Int? = null,

    @field:SerializedName("OrderNo")
    val orderNo: Int? = null,

    @field:SerializedName("Sku")
    val sku: String? = null,

    @field:SerializedName("CostPerItem")
    val costPerItem: Double? = null,

    @field:SerializedName("Inventory")
    val inventory: Int? = null,

    @field:SerializedName("GroupId")
    val groupId: Int? = null
) : Parcelable

@Parcelize
data class OptionItem(

    @field:SerializedName("OptionValue")
    val optionValue: String? = null,

    @field:SerializedName("OptionName")
    val optionName: String? = null
) : Parcelable
