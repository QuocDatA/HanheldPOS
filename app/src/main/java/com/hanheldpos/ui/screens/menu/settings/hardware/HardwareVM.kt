package com.hanheldpos.ui.screens.menu.settings.hardware

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class HardwareVM :BaseUiViewModel<HardwareUV>() {
    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }
}