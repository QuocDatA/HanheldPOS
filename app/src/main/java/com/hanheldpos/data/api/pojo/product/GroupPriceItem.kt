package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupPriceItem(
    val GroupTypeId: Int,
    val GroupGuid: String,
    val Product: List<GroupPriceProductItem>
) : Parcelable {
}

@Parcelize
data class GroupPriceProductItem(
    @field:SerializedName("ProductGuid")
    val ProductGuid: String,

    @field:SerializedName("ProductName")
    val ProductName: String,

    @field:SerializedName("ProductSKU")
    val ProductSKU: String,

    @field:SerializedName("ProductAmount")
    val ProductAmount: Double,

    @field:SerializedName("Variants")
    val Variants: List<GroupPriceProductVariantItem>
) : Parcelable {

}

@Parcelize
data class GroupPriceProductVariantItem(
    @field:SerializedName("GroupId")
    val GroupId: Int,

    @field:SerializedName("GroupName")
    val GroupName: String,

    @field:SerializedName("GroupSKU")
    val GroupSKU: String,

    @field:SerializedName("GroupAmount")
    val GroupAmount: Double
) : Parcelable {

}