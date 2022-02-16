package com.hanheldpos.ui.screens.product

import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.ui.base.BaseUserView

interface ProductDetailUV : BaseUserView {
    fun getBack();
    fun onAddCart(item: BaseProductInCart);
}