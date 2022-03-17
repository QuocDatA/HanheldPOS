package com.hanheldpos.ui.screens.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.payment.GiftCardResp
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.payment.PaymentSuggestionItem
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.payment.PaymentRepo
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.payment.PaymentOrder
import com.hanheldpos.model.payment.method.BasePayment
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PaymentVM : BaseUiViewModel<PaymentUV>() {
    private val paymentRepo = PaymentRepo()
    val balance = MutableLiveData<Double>()
    val listPaymentChosen = MutableLiveData(mutableListOf<PaymentOrder>())

    val totalPay = Transformations.map(listPaymentChosen) {
        return@map it.sumOf { paymentOrder ->
            paymentOrder.OverPay ?: 0.0
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

    fun processPaymentGiftCard(payment: BasePayment, cardNumber: String) {
        showLoading(true)
        paymentRepo.getValidGiftCard(
            UserHelper.getUserGuid(),
            cardNumber,
            callback = object : BaseRepoCallback<BaseResponse<GiftCardResp>> {
                override fun apiResponse(data: BaseResponse<GiftCardResp>?) {
                    showLoading(false)
                    if (data == null || data.DidError) {
                        showError(data?.ErrorMessage)
                    } else {
                        uiCallback?.onValidCardNumber(payment,data.Model?.Balance)
                    }
                }

                override fun showMessage(message: String?) {
                    showLoading(false)
                    showError(message)
                }
            })
    }

    fun openPaymentDetail() {
        uiCallback?.openPaymentDetail()
    }

}