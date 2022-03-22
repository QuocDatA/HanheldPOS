package com.hanheldpos.ui.screens.payment.completed

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PaymentCompletedVM : BaseUiViewModel<PaymentCompletedUV>() {
    val isAlreadyHasEmployee = MutableLiveData(false)
}