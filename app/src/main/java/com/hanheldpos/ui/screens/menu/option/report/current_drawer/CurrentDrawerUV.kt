package com.hanheldpos.ui.screens.menu.option.report.current_drawer

import com.hanheldpos.ui.base.BaseUserView

interface CurrentDrawerUV: BaseUserView {
    fun getBack()
    fun onOpenEndDrawer()
    fun onOpenPayInPayOut()
}