package com.hanheldpos.data.repository.customer

import com.hanheldpos.data.api.pojo.customer.CustomerActivitiesResp
import com.hanheldpos.data.api.pojo.customer.CustomerProfileResp
import com.hanheldpos.data.api.pojo.customer.CustomerSearchResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerRepo : BaseRepo() {
    fun getCustomersFromSearch(
        userGuid: String?,
        keyword: String?,
        pageNo: Int? = 1,
        callback: BaseRepoCallback<BaseResponse<List<CustomerSearchResp>>?>
    ) {
        callback.apiRequesting(true)
        customerService.getCustomerFromSearch(userGuid, keyword, pageNo)
            .enqueue(object : Callback<BaseResponse<List<CustomerSearchResp>>> {
                override fun onResponse(
                    call: Call<BaseResponse<List<CustomerSearchResp>>>,
                    response: Response<BaseResponse<List<CustomerSearchResp>>>
                ) {
                    callback.apiRequesting(false)
                    callback.apiResponse(getBodyResponse(response))
                }

                override fun onFailure(
                    call: Call<BaseResponse<List<CustomerSearchResp>>>,
                    t: Throwable
                ) {
                    callback.apiRequesting(false)
                    t.printStackTrace()
                    callback.showMessage(t.message)
                }
            })
    }

    fun getCustomerActivities(
        userGuid: String?,
        location: String?,
        pageNo: Int? = 1,
        pageSize: Int? = 4,
        customerId: String?,
        callback: BaseRepoCallback<BaseResponse<CustomerActivitiesResp>?>
    ) {
        callback.apiRequesting(true)
        customerService.getCustomerActivities(userGuid, location, pageNo, pageSize, customerId)
            .enqueue(object : Callback<BaseResponse<CustomerActivitiesResp>> {
                override fun onResponse(
                    call: Call<BaseResponse<CustomerActivitiesResp>>,
                    response: Response<BaseResponse<CustomerActivitiesResp>>
                ) {
                    callback.apiRequesting(false)
                    callback.apiResponse(getBodyResponse(response))
                }

                override fun onFailure(
                    call: Call<BaseResponse<CustomerActivitiesResp>>,
                    t: Throwable
                ) {
                    callback.apiRequesting(false)
                    t.printStackTrace()
                    callback.showMessage(t.message)
                }
            })
    }

    fun getCustomerProfileDetail(
        userGuid: String?,
        customerGuid: String?,
        callback: BaseRepoCallback<BaseResponse<CustomerProfileResp>>
    ) {
        customerService.getCustomerProfileDetail(userGuid, customerGuid)
            .enqueue(object : Callback<BaseResponse<CustomerProfileResp>> {
                override fun onResponse(
                    call: Call<BaseResponse<CustomerProfileResp>>,
                    response: Response<BaseResponse<CustomerProfileResp>>
                ) {
                    callback.apiRequesting(false)
                    callback.apiResponse(getBodyResponse(response))
                }

                override fun onFailure(
                    call: Call<BaseResponse<CustomerProfileResp>>,
                    t: Throwable
                ) {
                    callback.apiRequesting(false)
                    t.printStackTrace()
                    callback.showMessage(t.message)
                }

            })
    }
}