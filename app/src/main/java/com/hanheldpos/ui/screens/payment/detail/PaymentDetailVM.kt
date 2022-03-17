package com.hanheldpos.ui.screens.payment.detail

import androidx.lifecycle.ViewModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PaymentDetailVM : BaseUiViewModel<PaymentDetailUV>() {
    fun getBack() {
        uiCallback?.backPress()
    }
}