package com.hanheldpos.ui.screens.cart.customer.add_customer

import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class AddNewCustomerVM : BaseUiViewModel<AddNewCustomerUV>() {
    fun backPress() {
        uiCallback?.getBack()
    }

    fun onDateTimePick() {
        uiCallback?.onDateTimePick()
    }
}