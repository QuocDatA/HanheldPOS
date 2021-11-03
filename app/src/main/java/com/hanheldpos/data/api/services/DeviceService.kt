package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.device.DeviceCodeResp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DeviceService {
    @GET("Device")
    fun getDataByDeviceCode(
        @Query("AppCode") appCode: String?
    ): Call<DeviceCodeResp>
}