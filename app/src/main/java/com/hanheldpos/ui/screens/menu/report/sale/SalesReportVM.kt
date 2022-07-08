package com.hanheldpos.ui.screens.menu.report.sale

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.model.report.ReportFilterModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.menu.report.sale.adapter.NumberDayReportItem

class SalesReportVM : BaseUiViewModel<SalesReportUV>() {

    val isPreviewHistory = MutableLiveData(false)
    val saleReportFilter = MutableLiveData<ReportFilterModel>()

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
        uiCallback?.onOpenCustomizeReport()
    }

    fun onOpenHistoryRequest() {
        uiCallback?.onOpenHistoryRequest()
    }

    fun backPress() {
        uiCallback?.backPress()
    }
}