package com.hanheldpos.ui.screens.menu.option.report.sale.customize_report

import androidx.lifecycle.ViewModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class CustomizeReportVM : BaseUiViewModel<CustomizeReportUV>() {
    fun backPress() {
        uiCallback?.getBack()
    }
}