package com.hanheldpos.ui.screens.menu.option.report.drawer

import androidx.lifecycle.ViewModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class CurrentDrawerVM : BaseUiViewModel<CurrentDrawerUV>() {
    fun backPress() {
        uiCallback?.getBack()
    }
}