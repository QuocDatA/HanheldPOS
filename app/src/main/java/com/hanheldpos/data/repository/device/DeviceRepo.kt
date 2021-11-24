package com.hanheldpos.data.repository.device

import com.hanheldpos.data.api.pojo.device.DeviceCodeResp
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeviceRepo :  BaseRepo() {
    fun getDataByAppCode(pinTextStr: String, callback: BaseRepoCallback<DeviceCodeResp>) {
        callback.apiRequesting(true)
        deviceService.getDataByDeviceCode(pinTextStr).enqueue(object : Callback<DeviceCodeResp> {
            override fun onResponse(
                call: Call<DeviceCodeResp>,
                response: Response<DeviceCodeResp>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<DeviceCodeResp>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }
        })
    }
}