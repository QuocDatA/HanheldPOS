package com.hanheldpos.ui.screens.cart.payment.input

import androidx.lifecycle.ViewModel
import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PaymentInputVM : BaseUiViewModel<PaymentInputUV>() {
    fun onComplete(){
        uiCallback?.onComplete();
    }
    fun onCancel(){
        uiCallback?.onCancel();
    }
}