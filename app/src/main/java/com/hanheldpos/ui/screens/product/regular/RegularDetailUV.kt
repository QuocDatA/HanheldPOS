package com.hanheldpos.ui.screens.product.regular

import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.ui.base.BaseUserView

interface RegularDetailUV : BaseUserView {
    fun getBack();
    fun onAddCart(item: BaseProductInCart);
}