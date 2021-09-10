package com.hanheldpos.data.api.services.devicecode

import com.hanheldpos.data.api.pojo.setting.devicecode.DeviceCodeResp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SettingService {

    @GET("Device")
    fun getDataByDeviceCode(
        @Query("AppCode") appCode: String?
    ): Call<DeviceCodeResp>
}