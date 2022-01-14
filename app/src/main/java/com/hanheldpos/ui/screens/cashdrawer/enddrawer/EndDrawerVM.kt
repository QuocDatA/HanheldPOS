package com.hanheldpos.ui.screens.cashdrawer.enddrawer

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class EndDrawerVM : BaseUiViewModel<EndDrawerUV>() {
    fun backPress() {
        uiCallback?.getBack()
    }
}