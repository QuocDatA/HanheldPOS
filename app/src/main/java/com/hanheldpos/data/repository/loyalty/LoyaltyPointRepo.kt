package com.hanheldpos.data.repository.loyalty

import com.hanheldpos.data.api.pojo.loyalty.LoyaltyResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoyaltyPointRepo : BaseRepo() {
    fun postLoyaltyAsync(
        body: String,
        callback: BaseRepoCallback<BaseResponse<LoyaltyResp>>
    ) {
        callback.apiRequesting(true)
        loyaltyPointService.postCustomerLoyaltyPoint(body)
            .enqueue(object : Callback<BaseResponse<LoyaltyResp>> {
                override fun onResponse(
                    call: Call<BaseResponse<LoyaltyResp>>,
                    response: Response<BaseResponse<LoyaltyResp>>
                ) {
                    callback.apiRequesting(false)
                    callback.apiResponse(getBodyResponse(response))
                }

                override fun onFailure(call: Call<BaseResponse<LoyaltyResp>>, t: Throwable) {
                    callback.apiRequesting(false)
                    t.printStackTrace()
                    callback.showMessage(t.message)
                }
            })
    }
}