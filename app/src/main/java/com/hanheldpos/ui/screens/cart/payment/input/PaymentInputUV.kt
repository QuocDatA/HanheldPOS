package com.hanheldpos.ui.screens.cart.payment.input

import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.BaseUserView

interface PaymentInputUV: BaseUserView {
    fun onComplete()
    fun onCancel()
    fun onSwitch(keyBoardType: KeyBoardType)
}