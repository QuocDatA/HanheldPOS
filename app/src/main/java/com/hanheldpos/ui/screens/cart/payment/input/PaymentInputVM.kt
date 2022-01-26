package com.hanheldpos.ui.screens.cart.payment.input

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.extension.toNiceString
import com.hanheldpos.model.cart.payment.PaymentAppyTo
import com.hanheldpos.model.cart.payment.PaymentMethodType
import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PaymentInputVM : BaseUiViewModel<PaymentInputUV>() {
    val paymentMethodTitle = MutableLiveData<String>()

    fun onComplete() {
        uiCallback?.onComplete();
    }

    fun onCancel() {
        uiCallback?.onCancel();
    }

    fun clearInput() {
        uiCallback?.clearInput()
    }

    fun setUpKeyboard(paymentMethod: PaymentMethodResp, payable: Double): KeyBoardType {
        val keyBoardType: KeyBoardType
        when (paymentMethod.PaymentMethodType) {
            PaymentMethodType.CASH.value -> {
                keyBoardType = KeyBoardType.NumberOnly
                paymentMethodTitle.postValue(paymentMethod.Title)
            }
            PaymentMethodType.WALLET.value -> {
                keyBoardType = KeyBoardType.Text
                paymentMethodTitle.postValue("Input Card Number")
                uiCallback?.clearInput()
            }
            PaymentMethodType.GIFT_CARD.value -> {
                keyBoardType = KeyBoardType.Text
                paymentMethodTitle.postValue("Input Card Number")
                uiCallback?.clearInput()
            }
            else -> {
                if (paymentMethod.PaymentMethodType == PaymentAppyTo.SODEXO.value) {
                    keyBoardType = KeyBoardType.Text
                    paymentMethodTitle.postValue("Input Card Number")
                    uiCallback?.clearInput()
                } else {
                    keyBoardType = KeyBoardType.NumberOnly
                    paymentMethodTitle.postValue(paymentMethod.Title + " (Amount Due " + payable.toNiceString() + ")")
                }
            }
        }
        return keyBoardType
    }
}