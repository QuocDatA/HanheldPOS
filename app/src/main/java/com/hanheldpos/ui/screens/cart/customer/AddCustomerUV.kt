package com.hanheldpos.ui.screens.cart.customer

import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.ui.base.BaseUserView

interface AddCustomerUV : BaseUserView {
    fun getBack()
    fun loadCustomer(list : List<CustomerResp>)
}