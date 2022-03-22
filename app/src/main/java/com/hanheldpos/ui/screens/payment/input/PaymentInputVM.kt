package com.hanheldpos.ui.screens.payment.input

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.extension.toNiceString
import com.hanheldpos.model.payment.PaymentApplyTo
import com.hanheldpos.model.payment.PaymentMethodType
import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PaymentInputVM : BaseUiViewModel<PaymentInputUV>() {

    var inputAmount : Double = 0.0;
    var inputCardNumber : String = ""

    fun clearInput() {
        uiCallback?.clearInput()
    }

    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }

}