package com.hanheldpos.ui.screens.home.order

import android.view.View
import com.hanheldpos.ui.base.BaseUserView

interface OrderUV : BaseUserView {
    fun showCategoryDialog(isBackToTable: Boolean)
    fun showCart()
}