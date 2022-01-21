package com.hanheldpos.ui.screens.menu.option.report.current_drawer

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class CurrentDrawerVM : BaseUiViewModel<CurrentDrawerUV>() {
    fun backPress() {
        uiCallback?.getBack()
    }
    fun onOpenEndDrawer() {
        uiCallback?.onOpenEndDrawer()
    }
    fun onOpenPayInPayOut() {
        uiCallback?.onOpenPayInPayOut()
    }
}