package com.hanheldpos.data.repository.customer

import com.hanheldpos.data.api.pojo.customer.CustomerSearchResp
import com.hanheldpos.data.api.pojo.device.DeviceCodeResp
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerRepo : BaseRepo() {
    fun getCustomersFromSearch(
        userGuid : String?,
        keyword : String?,
        pageNo : Int? = 1,
        callback: BaseRepoCallback<CustomerSearchResp>
    ){
        callback.apiRequesting(true)
        customerService.getCustomerFromSearch(userGuid,keyword,pageNo).enqueue(object : Callback<CustomerSearchResp>{
            override fun onResponse(
                call: Call<CustomerSearchResp>,
                response: Response<CustomerSearchResp>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<CustomerSearchResp>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }
        })
    }
}