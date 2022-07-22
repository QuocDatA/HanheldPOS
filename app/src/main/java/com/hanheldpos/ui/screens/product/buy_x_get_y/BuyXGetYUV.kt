package com.hanheldpos.ui.screens.product.buy_x_get_y

import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.product.combo.ItemActionType
import com.hanheldpos.ui.base.BaseUserView

interface BuyXGetYUV : BaseUserView{
    fun onFragmentBackPressed()
    fun cartAdded(item : BaseProductInCart, action: ItemActionType)
    fun onDiscountBuyXGetYEntireOrder(discount: DiscountResp)
}