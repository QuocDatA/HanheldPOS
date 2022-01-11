package com.hanheldpos.ui.screens.product_new

import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.BaseUserView

interface ProductDetailNewUV : BaseUserView {
    fun getBack();
    fun onAddCart(item: BaseProductInCart);
}