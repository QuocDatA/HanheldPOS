package com.hanheldpos.model.product

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.order.ProductItem
import com.hanheldpos.data.api.pojo.product.ProductDetailResp
import com.hanheldpos.data.api.pojo.product.getPriceByExtra
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductCompleteModel(
    var productItem: ProductItem,
    var productDetail: ProductDetailResp? = null,
    var quantity: Int = 1,
    var note : String? = null
) : Parcelable,Cloneable {
    fun getPriceTotal() : Double{
        return productItem.price?.times(quantity)!! + (productDetail?.getPriceByExtra() ?: 0.0);
    }

}