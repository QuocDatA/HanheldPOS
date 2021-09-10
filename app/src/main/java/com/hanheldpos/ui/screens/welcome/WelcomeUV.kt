package com.hanheldpos.ui.screens.welcome

import com.hanheldpos.ui.base.BaseUserView

interface WelcomeUV : BaseUserView {
    fun openDeviceCode()
    fun openPinCode()
    fun showMessage(message : String?)
}