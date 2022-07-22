package com.hanheldpos.ui.screens.payment.input

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PaymentInputVM : BaseUiViewModel<PaymentInputUV>() {

    var inputAmount : Double = 0.0
    var inputCardNumber : String = ""

    fun clearInput() {
        uiCallback?.clearInput()
    }

    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }

}