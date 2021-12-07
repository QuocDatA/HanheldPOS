package com.hanheldpos.ui.screens.cart.customer

import androidx.lifecycle.ViewModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.base.viewmodel.BaseViewModel

class AddNewCustomerVM : BaseUiViewModel<AddNewCustomerUV>() {
    fun backPress() {
        uiCallback?.getBack()
    }

    fun onDateTimePick() {
        uiCallback?.onDateTimePick()
    }
}