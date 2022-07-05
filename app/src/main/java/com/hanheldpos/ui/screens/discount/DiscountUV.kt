package com.hanheldpos.ui.screens.discount

import com.hanheldpos.data.api.pojo.discount.DiscountCoupon
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.ui.base.BaseUserView

interface DiscountUV : BaseUserView {
    fun backPress()
    fun onScanner()
    fun onApplyDiscountCode(discount : List<DiscountCoupon>)
    fun onApplyDiscountAuto(discount : DiscountResp, applyTo: DiscApplyTo)
}