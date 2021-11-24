package com.hanheldpos.ui.screens.home.order.menu

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.home.order.OrderUV

class CategoryMenuVM : BaseUiViewModel<CategoryMenuUV>() {
    fun backPress() {
        uiCallback?.getBack()
    }
}