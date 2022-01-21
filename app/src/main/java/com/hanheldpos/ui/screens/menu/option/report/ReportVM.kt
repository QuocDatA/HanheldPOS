package com.hanheldpos.ui.screens.menu.option.report

import androidx.lifecycle.ViewModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class ReportVM : BaseUiViewModel<ReportUV>() {
    fun backPress() {
        uiCallback?.getBack()
    }
}