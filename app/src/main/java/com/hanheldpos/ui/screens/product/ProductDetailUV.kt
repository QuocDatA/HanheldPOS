package com.hanheldpos.ui.screens.product

import com.hanheldpos.model.product.ProductCompleteModel
import com.hanheldpos.ui.base.BaseUserView
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

interface ProductDetailUV : BaseUserView {
    fun onBack();
    fun onAddCart(item: ProductCompleteModel);
}