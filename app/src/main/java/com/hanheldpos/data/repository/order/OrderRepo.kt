package com.hanheldpos.data.repository.order

import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepo : BaseRepo() {
    fun getOrderSetting(
        userGuid: String?,
        locationGuid: String?,
        callback: BaseRepoCallback<OrderSettingResp?>
    ) {
        callback.apiRequesting(true);
        orderService.getOrderSettings(userGuid = userGuid, location = locationGuid).enqueue(object :
            Callback<OrderSettingResp?> {
            override fun onResponse(
                call: Call<OrderSettingResp?>,
                response: Response<OrderSettingResp?>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<OrderSettingResp?>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        })
    }
}