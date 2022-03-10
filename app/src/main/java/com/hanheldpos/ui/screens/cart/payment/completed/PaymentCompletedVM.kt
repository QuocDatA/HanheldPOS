package com.hanheldpos.ui.screens.cart.payment.completed

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PaymentCompletedVM : BaseUiViewModel<PaymentCompletedUV>() {
    val isAlreadyHasEmployee = MutableLiveData(false)
}