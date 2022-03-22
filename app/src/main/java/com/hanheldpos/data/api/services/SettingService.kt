package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.setting.SettingDeviceResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.model.setting.SettingDevicePut
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.PUT

interface SettingService {

    @PUT("settings/device/ids")
    @Headers("Accept: text/plain",
        "Content-Type: application/json")
    fun putSettingsDevice(
        @Body body : String
    ): Call<BaseResponse<String>>
}