package com.hanheldpos.model.home.order.menu

import com.hanheldpos.data.api.pojo.product.Product
import com.hanheldpos.model.home.order.ProductModeViewType

data class ProductMenuItem(
    val proOriginal: Product? = null,
    var uiType : ProductModeViewType = ProductModeViewType.Product
)