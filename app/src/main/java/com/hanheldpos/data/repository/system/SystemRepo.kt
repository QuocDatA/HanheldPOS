package com.hanheldpos.data.repository.system

import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.system.AddressTypeResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SystemRepo : BaseRepo() {
    fun getAddressTypes(
        callback: BaseRepoCallback<BaseResponse<List<AddressTypeResp>>>
    ) {
        callback.apiRequesting(true);
        systemService.getSystemAddressType().enqueue(object :
            Callback<BaseResponse<List<AddressTypeResp>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<AddressTypeResp>>>,
                response: Response<BaseResponse<List<AddressTypeResp>>>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<BaseResponse<List<AddressTypeResp>>>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        })
    }
}