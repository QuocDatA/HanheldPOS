package com.hanheldpos.ui.screens.menu.customers.customer_detail

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.customer.CustomerActivitiesResp
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.customer.CustomerRepo
import com.hanheldpos.model.UserHelper
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class CustomerDetailVM : BaseUiViewModel<CustomerDetailUV>() {
    val isLoading = MutableLiveData<Boolean>(false)
    val customer = MutableLiveData<CustomerResp>()
    private val customerRepo = CustomerRepo()
    fun fetchActivitiesOfCustomer(customerId: String?, pageNo: Int? = 1, keyRequest: Int) {
        customerRepo.getCustomerActivities(
            UserHelper.getUserGuid(),
            UserHelper.getLocationGuid(),
            pageNo = pageNo,
            customerId = customerId,
            callback = object : BaseRepoCallback<BaseResponse<CustomerActivitiesResp>?> {
                override fun apiRequesting(showLoading: Boolean) {
                    isLoading.postValue(showLoading)
                }

                override fun apiResponse(data: BaseResponse<CustomerActivitiesResp>?) {
                    if (data == null || data.DidError) {
                        uiCallback?.onLoadedActivities(mutableListOf(), false, keyRequest)
                    } else {
                        data.Model?.OrderList?.let {
                            uiCallback?.onLoadedActivities(
                                it,
                                true,
                                keyRequest
                            )
                        }
                    }
                }

                override fun showMessage(message: String?) {
                    uiCallback?.onLoadedActivities(mutableListOf(), false, keyRequest)
                }

            })
    }

    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }
}