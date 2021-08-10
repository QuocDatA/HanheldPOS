package com.hanheldpos.ui.screens.home.order

import com.hanheldpos.data.api.pojo.CategoryItem
import com.hanheldpos.data.api.pojo.ProductItem
import com.hanheldpos.ui.base.BaseUserView

interface OrderUV : BaseUserView {
    fun categoryListObserve(categoryList : List<CategoryItem> )
    fun showDropdownCategories(isShowed: Boolean);
    fun productListObserve(categoryListSelected: List<CategoryItem>)
    fun showProductSelected(productSelected : ProductItem)
}