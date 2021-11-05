package com.hanheldpos.ui.screens.cart.payment

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PaymentVM : BaseUiViewModel<PaymentUV>() {
    fun backPress() {
        uiCallback?.getBack()
    }

    fun initPaymentMethod(): List<PaymentFragment.FakeMethodModel> {
        val paymentMethodList: MutableList<PaymentFragment.FakeMethodModel> = mutableListOf();

        paymentMethodList.add(PaymentFragment.FakeMethodModel("Tien Mat", "#333333"))
        paymentMethodList.add(PaymentFragment.FakeMethodModel("Visa", "#00579F"))
        paymentMethodList.add(PaymentFragment.FakeMethodModel("Zalo Pay", "#0068FF"))
        paymentMethodList.add(PaymentFragment.FakeMethodModel("Master", "#999999"))
        paymentMethodList.add(PaymentFragment.FakeMethodModel("Cash Voucher", "#F79E1B"))
        paymentMethodList.add(PaymentFragment.FakeMethodModel("MoMo", "#AE2070"))
        paymentMethodList.add(PaymentFragment.FakeMethodModel("Grab", "#00B14F"))
        paymentMethodList.add(PaymentFragment.FakeMethodModel("Wallet", "#D6613D"))
        paymentMethodList.add(PaymentFragment.FakeMethodModel("Gift Card", "#999999"))
        paymentMethodList.add(PaymentFragment.FakeMethodModel("Gift Card", "#999999"))
        paymentMethodList.add(PaymentFragment.FakeMethodModel("Gift Card", "#999999"))
        paymentMethodList.add(PaymentFragment.FakeMethodModel("Gift Card", "#999999"))
        paymentMethodList.add(PaymentFragment.FakeMethodModel("Gift Card", "#999999"))
        

        return paymentMethodList
    }

    fun initPaymentSuggestion(): List<PaymentFragment.FakeMethodModel> {
        val paymentMethodList: MutableList<PaymentFragment.FakeMethodModel> = mutableListOf();

        paymentMethodList.add(PaymentFragment.FakeMethodModel("50,000", "#999999"))
        paymentMethodList.add(PaymentFragment.FakeMethodModel("100,000", "#999999"))
        paymentMethodList.add(PaymentFragment.FakeMethodModel("250,000", "#999999"))
        paymentMethodList.add(PaymentFragment.FakeMethodModel("500,000", "#999999"))

        return paymentMethodList
    }
}