package com.hanheldpos.ui.screens.product

import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.BaseUserView

interface ProductDetailUV : BaseUserView {
    fun getBack();
    fun onAddCart(item: BaseProductInCart);
}