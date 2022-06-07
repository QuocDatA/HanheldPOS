package com.hanheldpos.ui.screens.buy_x_get_y

import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.combo.ItemActionType
import com.hanheldpos.model.discount.DiscountUser
import com.hanheldpos.ui.base.BaseUserView

interface BuyXGetYUV : BaseUserView{
    fun onFragmentBackPressed()
    fun cartAdded(item : BaseProductInCart, action: ItemActionType);
    fun onDiscountBuyXGetYEntireOrder(discountUser: DiscountUser)
}