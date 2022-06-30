package com.hanheldpos.ui.screens.cart.customer.add_customer

import android.media.MediaDrm
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.ui.base.BaseUserView

interface AddCustomerUV : BaseUserView {
    fun onFragmentBackPressed()
    fun onLoadedCustomerView(list : List<CustomerResp>, isSuccess : Boolean, keyRequest: Int)
    fun onLoadedCustomerScan(list : List<CustomerResp>, isSuccess : Boolean, keyRequest: Int)
    fun onAddNewCustomer()
    fun onScanQrCode()
}