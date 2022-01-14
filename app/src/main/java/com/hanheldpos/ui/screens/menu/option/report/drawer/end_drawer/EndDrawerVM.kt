package com.hanheldpos.ui.screens.menu.option.report.drawer.end_drawer

import androidx.lifecycle.ViewModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class EndDrawerVM : BaseUiViewModel<EndDrawerUV>() {
    fun backPress() {
        uiCallback?.getBack()
    }
}