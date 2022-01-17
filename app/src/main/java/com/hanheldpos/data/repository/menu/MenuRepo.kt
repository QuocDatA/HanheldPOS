package com.hanheldpos.data.repository.menu

import com.hanheldpos.data.api.pojo.order.menu.MenuResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuRepo : BaseRepo() {
    fun getOrderMenu(
        userGuid: String?,
        locationGuid: String?,
        callback: BaseRepoCallback<BaseResponse<MenuResp>>
    ) {
        callback.apiRequesting(true);
        menuService.getOrderMenu(
            userGuid = userGuid,
            location = locationGuid
        ).enqueue(object : Callback<BaseResponse<MenuResp>> {
            override fun onResponse(call: Call<BaseResponse<MenuResp>>, response: Response<BaseResponse<MenuResp>>) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<BaseResponse<MenuResp>>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }
        })
    }
}