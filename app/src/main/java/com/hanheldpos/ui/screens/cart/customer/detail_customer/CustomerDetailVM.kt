package com.hanheldpos.ui.screens.cart.customer.detail_customer

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.PosApp
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.customer.CustomerProfileResp
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.system.AddressTypeResp
import com.hanheldpos.data.api.services.CustomerService
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.customer.CustomerRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.cart.CurCartData

class CustomerDetailVM : BaseUiViewModel<CustomerDetailUV>() {

    val customer = MutableLiveData<CustomerResp?>()
    val addressType = MutableLiveData<AddressTypeResp?>()
    val customerProfile = MutableLiveData<CustomerProfileResp>()
    private val customerRepo = CustomerRepo()

    fun backPress() {
        uiCallback?.backPress()
    }

    fun removeCustomer() {
        uiCallback?.removeCustomer()
    }

    fun getCustomerProfileDetail() {
        showLoading(true)
        customerRepo.getCustomerProfileDetail(
            DataHelper.userGuid(),
            customer.value?._id,
            callback = object : BaseRepoCallback<BaseResponse<CustomerProfileResp>> {
                override fun apiResponse(data: BaseResponse<CustomerProfileResp>?) {
                    if (data == null || data.DidError) {
                        showLoading(false)
                        showError(PosApp.instance.getString(R.string.failed_to_load_data))
                    } else {
                        customerProfile.value = data.Model
                        showLoading(false)
                    }
                }

                override fun showMessage(message: String?) {
                    showLoading(false)
                    showError(message)
                }
            })
    }
}