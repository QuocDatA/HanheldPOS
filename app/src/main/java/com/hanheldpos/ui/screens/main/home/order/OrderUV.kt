package com.hanheldpos.ui.screens.main.home.order

import com.hanheldpos.model.home.order.CategoryModel
import com.hanheldpos.ui.base.BaseUserView

interface OrderUV : BaseUserView {
    fun categoryListObserve(categoryList : List<CategoryModel> )
    fun showDropdownCategories(isShowed: Boolean);
}