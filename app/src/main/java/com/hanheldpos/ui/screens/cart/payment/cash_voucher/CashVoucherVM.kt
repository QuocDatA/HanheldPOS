package com.hanheldpos.ui.screens.cart.payment.cash_voucher

import androidx.lifecycle.ViewModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class CashVoucherVM : BaseUiViewModel<CashVoucherUV>() {
    fun backPress() {
        uiCallback?.getBack()
    }
}