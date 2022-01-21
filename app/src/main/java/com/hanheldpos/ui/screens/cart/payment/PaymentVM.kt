package com.hanheldpos.ui.screens.cart.payment

import androidx.fragment.app.activityViewModels
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.payment.PaymentSuggestionItem
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.payment.PaymentAppyTo
import com.hanheldpos.model.cart.payment.PaymentMethodType
import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.cart.CartDataVM

class PaymentVM : BaseUiViewModel<PaymentUV>() {

    fun backPress() {
        uiCallback?.getBack()
    }

    fun getPaymentMethods(): MutableList<PaymentMethodResp> {
        val paymentMethods: MutableList<PaymentMethodResp> =
            DataHelper.getPaymentMethodList()!!.toMutableList();
        return paymentMethods;
    }

    fun initPaymentSuggestion(): List<PaymentSuggestionItem> {
        val paymentSuggestionList = mutableListOf<PaymentSuggestionItem>(
            PaymentSuggestionItem(50000.0),
            PaymentSuggestionItem(100000.0),
            PaymentSuggestionItem(250000.0),
            PaymentSuggestionItem(500000.0),
        )
        return paymentSuggestionList
    }

    fun getPayment() {
        uiCallback?.getPayment()
    }
}