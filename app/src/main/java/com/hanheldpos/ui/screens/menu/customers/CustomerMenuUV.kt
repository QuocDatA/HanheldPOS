package com.hanheldpos.ui.screens.menu.customers

import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.customer.CustomerSearchResp
import com.hanheldpos.ui.base.BaseUserView

interface CustomerMenuUV : BaseUserView {
    fun onFragmentBackPressed()
    fun onLoadedCustomers(list  : List<CustomerResp>?, isSuccess : Boolean,keyRequest: Int)
}