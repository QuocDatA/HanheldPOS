package com.hanheldpos.data.repository.payment

import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentRepo : BaseRepo() {
    fun getPaymentMethods(
        userGuid: String?,
        callback: BaseRepoCallback<BaseResponse<List<PaymentMethodResp>>>
    ) {
        callback.apiRequesting(true);
        paymentService.getPaymentMethods(userGuid = userGuid).enqueue(object :
            Callback<BaseResponse<List<PaymentMethodResp>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<PaymentMethodResp>>>,
                response: Response<BaseResponse<List<PaymentMethodResp>>>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<BaseResponse<List<PaymentMethodResp>>>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        })
    }
}