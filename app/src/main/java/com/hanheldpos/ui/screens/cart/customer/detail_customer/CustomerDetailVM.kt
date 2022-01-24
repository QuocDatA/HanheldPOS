package com.hanheldpos.ui.screens.cart.customer.detail_customer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.time.DateTimeHelper

class CustomerDetailVM : BaseUiViewModel<CustomerDetailUV>() {

    val customer = MutableLiveData<CustomerResp?>();

    fun backPress(){
        uiCallback?.backPress()
    }

}