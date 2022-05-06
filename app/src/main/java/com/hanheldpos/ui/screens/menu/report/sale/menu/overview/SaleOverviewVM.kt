package com.hanheldpos.ui.screens.menu.report.sale.menu.overview

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.model.menu.report.ReportItem
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class SaleOverviewVM : BaseUiViewModel<SaleOverviewUV>() {
    val isShowDetail = MutableLiveData(false)
    fun getReportItems() : List<ReportItem> {
        return mutableListOf(
            ReportItem(title = "18,135,000", sub = "Gross Sales"),
            ReportItem(title = "18,135,000", sub = "Gross Sales"),
            ReportItem(title = "18,135,000", sub = "Gross Sales"),
            ReportItem(title = "18,135,000", sub = "Gross Sales"),
            ReportItem(title = "18,135,000", sub = "Gross Sales"),
            ReportItem(title = "18,135,000", sub = "Gross Sales"),
        )
    }
}