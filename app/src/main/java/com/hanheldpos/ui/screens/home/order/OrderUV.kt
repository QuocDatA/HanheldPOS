package com.hanheldpos.ui.screens.home.order

import com.hanheldpos.ui.base.BaseUserView

interface OrderUV : BaseUserView {
    fun showCategoryDialog(isGoBackTable : Boolean = false)
    fun showCart()
}