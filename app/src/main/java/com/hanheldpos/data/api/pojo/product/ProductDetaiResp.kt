package com.hanheldpos.data.api.pojo.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hanheldpos.data.api.pojo.order.ProductModifierItem

import com.hanheldpos.data.api.pojo.product.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDetailResp(

    @field:SerializedName("ProductModifierItem")
    val listSizeOptions: List<ProductModifierItem?>? = null,



    ) : Parcelable , Cloneable{

    public override fun clone(): ProductDetailResp {
        return copy()
    }
}


