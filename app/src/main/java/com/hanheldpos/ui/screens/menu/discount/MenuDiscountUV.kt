package com.hanheldpos.ui.screens.menu.discount

import com.hanheldpos.data.api.pojo.discount.DiscountCoupon
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.ui.base.BaseUserView

interface MenuDiscountUV : BaseUserView {
    fun onFragmentBackPressed()
    fun loadDiscountCode(list: List<DiscountResp>)
    fun updateDiscountCouponCode(discountCouponList: List<DiscountCoupon>?)
    fun openDiscountBuyXGetY(discount: DiscountResp)
}