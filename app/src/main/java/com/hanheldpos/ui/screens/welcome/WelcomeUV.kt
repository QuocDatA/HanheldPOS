package com.hanheldpos.ui.screens.welcome

import com.hanheldpos.data.api.pojo.welcome.WelcomeRespModel
import com.hanheldpos.ui.base.BaseUserView

interface WelcomeUV : BaseUserView {
    fun openDeviceCode()
    fun openPinCode()
    fun showMessage(message : String?)
    fun updateUI(welcomeResp : WelcomeRespModel?)
}