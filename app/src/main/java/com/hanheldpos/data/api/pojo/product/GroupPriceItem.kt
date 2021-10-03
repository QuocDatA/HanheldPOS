package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupPriceItem(
    @field:SerializedName("GroupTypeId")
    val groupTypeID: Int,

    @field:SerializedName("GroupGuid")
    val groupGUID: String,

    @field:SerializedName("Product")
    val product: List<GroupPriceProductItem>
) : Parcelable {
}

@Parcelize
data class GroupPriceProductItem(
    @field:SerializedName("ProductGuid")
    val productGUID: String,

    @field:SerializedName("ProductName")
    val productName: String,

    @field:SerializedName("ProductSKU")
    val productSKU: String,

    @field:SerializedName("ProductAmount")
    val productAmount: Double,

    @field:SerializedName("Variants")
    val variants: List<GroupPriceProductVariantItem>
) : Parcelable {

}

@Parcelize
data class GroupPriceProductVariantItem(
    @field:SerializedName("GroupId")
    val groupID: Int,

    @field:SerializedName("GroupName")
    val groupName: String,

    @field:SerializedName("GroupSKU")
    val groupSKU: String,

    @field:SerializedName("GroupAmount")
    val groupAmount: Double
) : Parcelable {

}