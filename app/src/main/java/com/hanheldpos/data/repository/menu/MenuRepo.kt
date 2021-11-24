package com.hanheldpos.data.repository.menu

import com.hanheldpos.data.api.pojo.order.menu.OrderMenuResp
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuRepo : BaseRepo() {
    fun getOrderMenu(
        userGuid: String?,
        locationGuid: String?,
        callback: BaseRepoCallback<OrderMenuResp?>
    ) {
        callback.apiRequesting(true);
        menuService.getOrderMenu(
            userGuid = userGuid,
            location = locationGuid
        ).enqueue(object : Callback<OrderMenuResp> {
            override fun onResponse(call: Call<OrderMenuResp>, response: Response<OrderMenuResp>) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<OrderMenuResp>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }
        })
    }
}