package com.hanheldpos.ui.screens.discount.discount_type.discount_code

import com.hanheldpos.data.api.pojo.discount.CouponDiscountResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.ui.base.BaseUserView

interface DiscountCodeUV : BaseUserView {
    fun loadDataDiscountCode(list : List<DiscountResp>);
    fun updateDiscountCouponCode(discount: CouponDiscountResp)
}