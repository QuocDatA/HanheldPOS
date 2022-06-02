package com.hanheldpos.ui.screens.menu.report.sale

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.menu.report.sale.adapter.NumberDayReportItem

class SalesReportVM : BaseUiViewModel<SalesReportUV>() {


    fun initNumberDaySelected(): MutableList<NumberDayReportItem> {
        return mutableListOf(
            NumberDayReportItem("1D", 1),
            NumberDayReportItem("2D", 2),
            NumberDayReportItem("3D", 3),
            NumberDayReportItem("5D", 5),
            NumberDayReportItem("1W", 7),
        )
    }

    fun onOpenCustomizeReport() {
        uiCallback?.onOpenCustomizeReport();
    }

    fun backPress() {
        uiCallback?.backPress();
    }
}