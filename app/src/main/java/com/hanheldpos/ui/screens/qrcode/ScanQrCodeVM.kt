package com.hanheldpos.ui.screens.qrcode

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class ScanQrCodeVM : BaseUiViewModel<ScanQrCodeUV>() {
    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }
}