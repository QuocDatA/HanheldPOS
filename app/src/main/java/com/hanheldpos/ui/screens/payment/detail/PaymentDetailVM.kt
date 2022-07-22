package com.hanheldpos.ui.screens.payment.detail

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PaymentDetailVM : BaseUiViewModel<PaymentDetailUV>() {
    fun getBack() {
        uiCallback?.backPress()
    }
}