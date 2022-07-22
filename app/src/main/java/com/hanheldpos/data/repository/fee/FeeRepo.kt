package com.hanheldpos.data.repository.fee

import com.hanheldpos.data.api.pojo.fee.FeeResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeeRepo : BaseRepo() {
    fun getFees(
        userGuid: String?,
        locationGuid: String?,
        callback: BaseRepoCallback<BaseResponse<FeeResp>?>
    )
    {
        callback.apiRequesting(true)
        feeService.getFees(
            userGuid = userGuid,
            location = locationGuid
        ).enqueue(object : Callback<BaseResponse<FeeResp>?> {
            override fun onResponse(call: Call<BaseResponse<FeeResp>?>, response: Response<BaseResponse<FeeResp>?>) {
                callback.apiRequesting(false)
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<BaseResponse<FeeResp>?>, t: Throwable) {
                callback.apiRequesting(false)
                t.printStackTrace()
                callback.showMessage(t.message)
            }

        })
    }
}