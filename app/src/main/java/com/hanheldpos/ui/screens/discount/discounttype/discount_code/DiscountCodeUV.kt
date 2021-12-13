package com.hanheldpos.ui.screens.discount.discounttype.discount_code

import com.hanheldpos.data.api.pojo.discount.DiscountItem
import com.hanheldpos.ui.base.BaseUserView

interface DiscountCodeUV : BaseUserView {
    fun loadDataDiscountCode(list : List<DiscountItem>);
}