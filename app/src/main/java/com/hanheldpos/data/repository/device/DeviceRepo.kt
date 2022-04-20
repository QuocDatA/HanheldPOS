package com.hanheldpos.data.repository.device

import com.hanheldpos.data.api.pojo.device.DeviceCodeResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeviceRepo :  BaseRepo() {
    fun getDataByAppCode(pinTextStr: String, uuid: String, callback: BaseRepoCallback<BaseResponse<List<DeviceCodeResp>>>) {
        callback.apiRequesting(true)
        deviceService.getDataByDeviceCode(pinTextStr, uuid).enqueue(object : Callback<BaseResponse<List<DeviceCodeResp>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<DeviceCodeResp>>>,
                response: Response<BaseResponse<List<DeviceCodeResp>>>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<BaseResponse<List<DeviceCodeResp>>>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }
        })
    }
}