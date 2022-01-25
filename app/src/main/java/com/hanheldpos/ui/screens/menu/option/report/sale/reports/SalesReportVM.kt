package com.hanheldpos.ui.screens.menu.option.report.sale.reports

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class SalesReportVM : BaseUiViewModel<SalesReportUV>() {


    fun onSyncOrders() {

    }

    fun backPress(){
        uiCallback?.backPress();
    }
}