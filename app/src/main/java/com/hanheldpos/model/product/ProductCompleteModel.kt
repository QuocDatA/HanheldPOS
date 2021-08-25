package com.hanheldpos.model.product

import android.os.Parcelable
import com.hanheldpos.data.api.pojo.order.ProductItem
import com.hanheldpos.data.api.pojo.product.ProductDetailResp
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductCompleteModel(
    var productItem: ProductItem? = null,
    var productDetail : ProductDetailResp? = null,
    var quantity : Int? = 0,
) : Parcelable,Cloneable {
}