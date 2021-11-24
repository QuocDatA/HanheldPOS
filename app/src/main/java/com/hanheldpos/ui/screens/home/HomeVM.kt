package com.hanheldpos.ui.screens.home

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class HomeVM : BaseUiViewModel<HomeUV>() {
    fun openSelectMenu() {
        uiCallback?.openSelectMenu()
    }
}