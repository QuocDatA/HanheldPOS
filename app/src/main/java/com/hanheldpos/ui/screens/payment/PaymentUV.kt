package com.hanheldpos.ui.screens.payment

import com.hanheldpos.ui.base.BaseUserView

interface PaymentUV : BaseUserView {
    fun getBack()
    fun openPaymentDetail()
    fun onFragmentBackPressed()
}