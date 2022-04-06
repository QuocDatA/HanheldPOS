package com.hanheldpos.ui.screens.cart

import com.hanheldpos.data.api.pojo.discount.CouponDiscountResp
import com.hanheldpos.data.api.pojo.discount.DiscountCoupon
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.floor.FloorTable
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.CartModel

object CurCartData {
    var cartModel: CartModel? = null
    var tableFocus: FloorTable? = null
}