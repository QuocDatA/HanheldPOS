package com.hanheldpos.ui.screens.payment.voucher

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class VoucherVM : BaseUiViewModel<VoucherUV>() {
    fun getBack() {
        uiCallback?.onFragmentBackPressed()
    }
}