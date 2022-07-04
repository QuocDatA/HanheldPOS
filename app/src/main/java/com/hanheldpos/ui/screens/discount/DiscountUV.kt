package com.hanheldpos.ui.screens.discount

import com.hanheldpos.data.api.pojo.discount.DiscountCoupon
import com.hanheldpos.ui.base.BaseUserView

interface DiscountUV : BaseUserView {
    fun backPress();
    fun onScanQrCode()
    fun onApplyDiscountCode(discount : List<DiscountCoupon>)
}