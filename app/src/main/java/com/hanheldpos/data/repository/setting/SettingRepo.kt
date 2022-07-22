package com.hanheldpos.data.repository.setting

import com.hanheldpos.data.api.pojo.setting.firebase.FirebaseSetting
import com.hanheldpos.data.api.pojo.setting.hardware.HardwareSetting
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingRepo : BaseRepo() {
    fun putSettingDeviceIds(
        body: String,
        callback: BaseRepoCallback<BaseResponse<String>>
    ) {
        callback.apiRequesting(true)
        settingService.putSettingsDevice(body).enqueue(object :
            Callback<BaseResponse<String>> {
            override fun onResponse(
                call: Call<BaseResponse<String>>,
                response: Response<BaseResponse<String>>
            ) {
                callback.apiRequesting(false)
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<BaseResponse<String>>, t: Throwable) {
                callback.apiRequesting(false)
                t.printStackTrace()
                callback.showMessage(t.message)
            }

        })
    }

    fun getFirebaseSetting(
        userGuid: String?,
        localeGuid: String?,
        deviceGuid: String?,
        callback: BaseRepoCallback<BaseResponse<FirebaseSetting>>
    ) {
        callback.apiRequesting(true)
        settingService.getFirebaseSetting(userGuid, localeGuid, deviceGuid).enqueue(object :
            Callback<BaseResponse<FirebaseSetting>> {
            override fun onResponse(
                call: Call<BaseResponse<FirebaseSetting>>,
                response: Response<BaseResponse<FirebaseSetting>>
            ) {
                callback.apiRequesting(false)
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<BaseResponse<FirebaseSetting>>, t: Throwable) {
                callback.apiRequesting(false)
                t.printStackTrace()
                callback.showMessage(t.message)
            }
        })
    }

    fun getHardwareSetting(
        userGuid: String?,
        localeGuid: String?,
        deviceGuid: String?,
        callback: BaseRepoCallback<BaseResponse<HardwareSetting>>
    ) {
        callback.apiRequesting(true)
        settingService.getHardwareSetting(userGuid, localeGuid, deviceGuid).enqueue(object :
            Callback<BaseResponse<HardwareSetting>> {
            override fun onResponse(
                call: Call<BaseResponse<HardwareSetting>>,
                response: Response<BaseResponse<HardwareSetting>>
            ) {
                callback.apiRequesting(false)
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<BaseResponse<HardwareSetting>>, t: Throwable) {
                callback.apiRequesting(false)
                t.printStackTrace()
                callback.showMessage(t.message)
            }
        })
    }
}