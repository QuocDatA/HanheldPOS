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

    fun searchCustomer(
        keyword: String?,
        pageNo: Int? = 1,
        keyRequest: Int,
        isScan: Boolean? = false
    ) {
        val userGuid = UserHelper.getUserGuid()
        repo?.getCustomersFromSearch(
            userGuid = userGuid,
            keyword = keyword,
            pageNo = pageNo,
            object : BaseRepoCallback<BaseResponse<List<CustomerSearchResp>>?> {
                override fun apiRequesting(showLoading: Boolean) {
                    if (isScan != true)
                        isLoading.postValue(showLoading)
                }

                override fun apiResponse(data: BaseResponse<List<CustomerSearchResp>>?) {
                    if (data == null || data.DidError || data.Model == null) {
                        if (isScan != true) {
                            uiCallback?.onLoadedCustomerView(mutableListOf(), false, keyRequest)
                        } else uiCallback?.onLoadedCustomerScan(mutableListOf(), false, keyRequest)
                    } else {
                        data.Model.firstOrNull()?.List?.let {
                            if (isScan != true) {
                                uiCallback?.onLoadedCustomerView(
                                    it,
                                    true,
                                    keyRequest
                                )
                            } else {
                                uiCallback?.onLoadedCustomerScan(
                                    it,
                                    true,
                                    keyRequest,
                                )
                            }
                        }
                    }
                }

                override fun showMessage(message: String?) {
                    if (isScan != true) {
                        uiCallback?.onLoadedCustomerView(mutableListOf(), false, keyRequest)
                    } else {
                        uiCallback?.onLoadedCustomerScan(mutableListOf(), false, keyRequest)
                    }
                }
            })
    }

    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }

    fun onScanner() {
        uiCallback?.onScanner()
    }


    fun onAddNewCustomer() {
        uiCallback?.onAddNewCustomer()
    }
}