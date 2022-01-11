package com.hanheldpos.ui.screens.cart.payment.input

import com.hanheldpos.ui.base.BaseUserView

interface PaymentInputUV: BaseUserView {
    fun onComplete()
    fun onCancel()
}