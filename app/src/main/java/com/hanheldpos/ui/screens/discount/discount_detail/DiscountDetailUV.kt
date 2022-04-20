package com.hanheldpos.ui.screens.discount.discount_detail

import com.hanheldpos.ui.base.BaseUserView

interface DiscountDetailUV: BaseUserView {
    fun getBack()
    fun showReqProduct(title: String,list : List<Any>)
}