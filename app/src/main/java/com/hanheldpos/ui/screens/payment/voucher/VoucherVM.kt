package com.hanheldpos.ui.screens.payment.voucher

import androidx.lifecycle.ViewModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class VoucherVM : BaseUiViewModel<VoucherUV>() {
    fun getBack() {
        uiCallback?.onFragmentBackPressed()
    }
}