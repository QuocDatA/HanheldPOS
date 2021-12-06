package com.hanheldpos.data.repository.setting

import com.hanheldpos.data.api.pojo.setting.SettingDeviceResp
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.model.setting.SettingDevicePut
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingRepo : BaseRepo() {
    fun putSettingDeviceIds(
        body : String,
        callback: BaseRepoCallback<SettingDeviceResp>
    ) {
        callback.apiRequesting(true);
        settingService.putSettingsDevice(body).enqueue(object :
            Callback<SettingDeviceResp> {
            override fun onResponse(
                call: Call<SettingDeviceResp>,
                response: Response<SettingDeviceResp>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<SettingDeviceResp>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        })
    }
}