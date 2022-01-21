package com.hanheldpos.ui.screens.menu.option.report.current_drawer

import com.hanheldpos.data.api.pojo.cashdrawer.report.ReportCashDrawerResp
import com.hanheldpos.ui.base.BaseUserView

interface CurrentDrawerUV: BaseUserView {
    fun getBack()
    fun onOpenEndDrawer()
    fun onOpenPayInPayOut()
    fun showInfoCurrentDrawer(report : ReportCashDrawerResp?);
}