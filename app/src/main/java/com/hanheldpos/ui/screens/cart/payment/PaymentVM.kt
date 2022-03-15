package com.hanheldpos.ui.screens.cart.payment

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.payment.PaymentSuggestionItem
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cart.payment.PaymentMethodType
import com.hanheldpos.model.cart.payment.PaymentOrder
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.time.DateTimeHelper
import java.util.*

class PaymentVM : BaseUiViewModel<PaymentUV>() {

    val balance = MutableLiveData<Double>()
    val listPaymentChosen = MutableLiveData(mutableListOf<PaymentOrder>())

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

    fun completedPayment(alreadyBill : Boolean,callback : PaymentFragment.PaymentCallback) {
        if (!listPaymentChosen.value.isNullOrEmpty()) {
            uiCallback?.onFragmentBackPressed()
            callback.onPaymentComplete(
                listPaymentChosen.value!!
            )
            if (alreadyBill) callback.onPayment(true)
        }
    }

    fun openPaymentDetail() {
        uiCallback?.openPaymentDetail()
    }

    fun getPayment() {
        uiCallback?.getPayment()
    }
}