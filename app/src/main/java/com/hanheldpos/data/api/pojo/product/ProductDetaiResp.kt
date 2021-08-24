package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hanheldpos.data.api.pojo.product.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDetailResp(

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
data class ProductOption(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("checked")
    val checked: Boolean? = null,
) : Parcelable

@Parcelize
data class ProductOptionExtra(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("quantity")
    val quantity: Int? = null,
) : Parcelable

@Parcelize
data class ModelItem(

    @field:SerializedName("SizeOptions")
    val listSizeOptions: List<ProductOption?>? = null,

    @field:SerializedName("CookOptions")
    val listCookOptions: List<ProductOption?>? = null,

    @field:SerializedName("SauceOptions")
    val listSauceOptions: List<ProductOption?>? = null,

    @field:SerializedName("ExtraOption")
    val listExtraOptions: List<ProductOptionExtra?>? = null,
) : Parcelable