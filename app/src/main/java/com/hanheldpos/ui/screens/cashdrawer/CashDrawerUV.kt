package com.hanheldpos.ui.screens.cashdrawer

import com.hanheldpos.ui.base.BaseUserView

interface CashDrawerUV : BaseUserView {
    fun backPress();
    fun goMain() : Unit {};
}