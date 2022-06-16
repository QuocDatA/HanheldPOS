package com.hanheldpos.ui.screens.cart.customer.add_customer

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.customer.CustomerSearchResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.customer.CustomerRepo
import com.hanheldpos.model.UserHelper
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel

class AddCustomerVM : BaseRepoViewModel<CustomerRepo, AddCustomerUV>() {

    val isLoading = MutableLiveData<Boolean>(false)

    override fun createRepo(): CustomerRepo {
        return CustomerRepo()
    }

    fun searchCustomer(keyword: String?, pageNo: Int? = 1, keyRequest: Int) {
        val userGuid = UserHelper.getUserGuid()
        repo?.getCustomersFromSearch(
            userGuid = userGuid,
            keyword = keyword,
            pageNo = pageNo,
            object : BaseRepoCallback<BaseResponse<List<CustomerSearchResp>>?> {
                override fun apiRequesting(showLoading: Boolean) {
                    isLoading.postValue(showLoading)
                }

                override fun apiResponse(data: BaseResponse<List<CustomerSearchResp>>?) {
                    if (data == null || data.DidError) {
                        uiCallback?.loadCustomer(mutableListOf(), false, keyRequest)
                    } else {
                        data.Model?.firstOrNull()?.List?.let {
                            uiCallback?.loadCustomer(
                                it,
                                true,
                                keyRequest
                            )
                        }
                    }
                }

                override fun showMessage(message: String?) {
                    uiCallback?.loadCustomer(mutableListOf(), false, keyRequest)
                }
            })
    }

    fun backPress() {
        uiCallback?.getBack()
    }

    fun onAddNewCustomer() {
        uiCallback?.onAddNewCustomer()
    }
}