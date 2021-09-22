package com.hanheldpos.ui.screens.product

import com.hanheldpos.model.cart.order.OrderItemModel
import com.hanheldpos.model.product.ExtraDoneModel
import com.hanheldpos.ui.base.BaseUserView

interface ProductDetailUV : BaseUserView {
    fun onBack();
    fun onAddCart(item: ExtraDoneModel);
}