package com.hanheldpos.ui.screens.cart.payment.input

import androidx.lifecycle.ViewModel
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.model.cart.payment.PaymentAppyTo
import com.hanheldpos.model.cart.payment.PaymentMethodType
import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PaymentInputVM : BaseUiViewModel<PaymentInputUV>() {
    fun onComplete(){
        uiCallback?.onComplete();
    }
    fun onCancel(){
        uiCallback?.onCancel();
    }
    fun setUpKeyboard(paymentMethodResp: PaymentMethodResp): KeyBoardType{
        val keyBoardType: KeyBoardType
        when(paymentMethodResp.PaymentMethodType){
            PaymentMethodType.CASH.value -> {
                keyBoardType = KeyBoardType.NumberOnly
            }
            PaymentMethodType.WALLET.value -> {
                keyBoardType = KeyBoardType.Text
            }
            PaymentMethodType.GIFT_CARD.value -> {
                keyBoardType = KeyBoardType.Text
            }
            else -> {
                keyBoardType = when(paymentMethodResp.ApplyToId) {
                    PaymentAppyTo.SODEXO.value -> {
                        KeyBoardType.Text
                    }
                    else -> {
                        KeyBoardType.NumberOnly
                    }
                }
            }
        }
        return keyBoardType
    }
}