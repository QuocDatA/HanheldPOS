package com.hanheldpos.ui.screens.menu.settings.hardware.hardware_detail

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class HardwareDetailVM : BaseUiViewModel<HardwareDetailUV>() {
    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }
}