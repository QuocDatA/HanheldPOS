package com.hanheldpos.ui.screens.product

import com.hanheldpos.model.product.BaseProductInCart
import com.hanheldpos.ui.base.BaseUserView

interface ProductDetailUV : BaseUserView {
    fun onBack();
    fun onAddCart(item: BaseProductInCart);
}