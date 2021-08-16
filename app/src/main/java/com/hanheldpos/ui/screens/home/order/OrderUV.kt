package com.hanheldpos.ui.screens.home.order

import android.view.View
import com.hanheldpos.data.api.pojo.CategoryItem
import com.hanheldpos.data.api.pojo.ProductItem
import com.hanheldpos.ui.base.BaseUserView

interface OrderUV : BaseUserView {
    fun changeCategoryPageView(view: View?);
}