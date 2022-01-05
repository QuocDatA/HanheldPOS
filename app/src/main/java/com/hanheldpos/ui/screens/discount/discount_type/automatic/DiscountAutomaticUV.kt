package com.hanheldpos.ui.screens.discount.discount_type.automatic

import com.hanheldpos.data.api.pojo.discount.DiscountItem
import com.hanheldpos.ui.base.BaseUserView

interface DiscountAutomaticUV : BaseUserView {
    fun loadDataDiscountCode(list : List<DiscountItem>);
}