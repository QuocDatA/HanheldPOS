package com.hanheldpos.data.repository.payment

import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.payment.PaymentsResp
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentRepo : BaseRepo() {
    fun getPaymentMethods(
        userGuid: String?,
        callback: BaseRepoCallback<PaymentsResp>
    ) {
        callback.apiRequesting(true);
        paymentService.getPaymentMethods(userGuid = userGuid).enqueue(object :
            Callback<PaymentsResp> {
            override fun onResponse(
                call: Call<PaymentsResp>,
                response: Response<PaymentsResp>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<PaymentsResp>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        })
    }
}