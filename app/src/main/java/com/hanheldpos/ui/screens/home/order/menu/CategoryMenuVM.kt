package com.hanheldpos.ui.screens.home.order.menu

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class CategoryMenuVM : BaseUiViewModel<CategoryMenuUV>() {
    fun backPress() {
        uiCallback?.getBack(isSelected = false)
    }
}