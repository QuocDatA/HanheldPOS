package com.hanheldpos.data.repository.order

import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.model.order.OrderSubmitResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderAsyncRepo : BaseRepo() {
    fun postOrderSubmit(
        body : String,
        callback: BaseRepoCallback<BaseResponse<OrderSubmitResp>>
    ) {
        callback.apiRequesting(true);
        orderAsyncService.postOrderSubmit(body).enqueue(object :
            Callback<BaseResponse<OrderSubmitResp>> {
            override fun onResponse(
                call: Call<BaseResponse<OrderSubmitResp>?>,
                response: Response<BaseResponse<OrderSubmitResp>?>
            ) {

                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<BaseResponse<OrderSubmitResp>>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        })
    }
}