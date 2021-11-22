package com.hanheldpos.data.repository.fee

import com.hanheldpos.data.api.pojo.fee.FeeResp
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeeRepo : BaseRepo() {
    fun getFees(
        userGuid: String?,
        locationGuid: String?,
        callback: BaseRepoCallback<FeeResp?>
    )
    {
        callback.apiRequesting(true);
        feeService.getFees(
            userGuid = userGuid,
            location = locationGuid
        ).enqueue(object : Callback<FeeResp> {
            override fun onResponse(call: Call<FeeResp>, response: Response<FeeResp>) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<FeeResp>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        })
    }
}