package com.hanheldpos.ui.screens.main

import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class MainVM : BaseUiViewModel<MainUV>() {
    fun initView() {
        if (DataHelper.deviceCodeLocalStorage != null) {
            uiCallback?.openPinCode()
        } else uiCallback?.openWelcome()
    }

}