package com.hanheldpos.ui.screens.cart.payment

import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.payment.PaymentSuggestionItem
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PaymentVM : BaseUiViewModel<PaymentUV>() {

    fun backPress() {
        uiCallback?.getBack()
    }

    fun getPaymentMethods(): MutableList<PaymentMethodResp> {
        val paymentMethods: MutableList<PaymentMethodResp> =
            DataHelper.paymentMethodsLocalStorage!!.toMutableList()
        return paymentMethods
    }

    fun initPaymentSuggestion(): List<PaymentSuggestionItem> {
        return mutableListOf(
            PaymentSuggestionItem(50000.0),
            PaymentSuggestionItem(100000.0),
            PaymentSuggestionItem(250000.0),
            PaymentSuggestionItem(500000.0),
        )
    }

    fun getPayment() {
        uiCallback?.getPayment()
    }
}