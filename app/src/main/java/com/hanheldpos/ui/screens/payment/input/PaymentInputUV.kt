package com.hanheldpos.ui.screens.payment.input

import com.hanheldpos.model.keyboard.KeyBoardType
import com.hanheldpos.ui.base.BaseUserView

interface PaymentInputUV: BaseUserView {
    fun clearInput()
    fun onFragmentBackPressed()
}