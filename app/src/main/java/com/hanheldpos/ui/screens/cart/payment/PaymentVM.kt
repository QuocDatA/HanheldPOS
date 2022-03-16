package com.hanheldpos.ui.screens.cart.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.payment.PaymentSuggestionItem
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.cart.payment.PaymentOrder
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PaymentVM : BaseUiViewModel<PaymentUV>() {

    val balance = MutableLiveData<Double>()
    val listPaymentChosen = MutableLiveData(mutableListOf<PaymentOrder>())

    val totalPay = Transformations.map(listPaymentChosen) {
        return@map it.sumOf { paymentOrder ->
            paymentOrder.OverPay?: 0.0
        }
    }
    fun backPress() {
        uiCallback?.getBack()
    }

    fun getPaymentMethods(): MutableList<PaymentMethodResp> {
        return DataHelper.paymentMethodsLocalStorage!!.toMutableList()
    }

    fun initPaymentSuggestion(): List<PaymentSuggestionItem> {
        return mutableListOf(
            PaymentSuggestionItem(50000.0),
            PaymentSuggestionItem(100000.0),
            PaymentSuggestionItem(250000.0),
            PaymentSuggestionItem(500000.0),
        )
    }

    fun completedPayment(alreadyBill: Boolean, callback: PaymentFragment.PaymentCallback) {
        uiCallback?.onFragmentBackPressed()
        callback.onPaymentComplete(
            listPaymentChosen.value!!
        )
        if (alreadyBill) callback.onPayment(true)
    }

    fun addPaymentChosen(payment: PaymentOrder) {
        listPaymentChosen.value?.apply {
            this.add(payment)
        }
        listPaymentChosen.notifyValueChange()
    }

    fun openPaymentDetail() {
        uiCallback?.openPaymentDetail()
    }

}