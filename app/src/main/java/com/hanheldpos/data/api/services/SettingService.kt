package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.setting.firebase.FirebaseSetting
import com.hanheldpos.data.api.pojo.setting.hardware.HardwareSetting
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.*

interface SettingService {

    @PUT("settings/device/ids")
    @Headers("Accept: text/plain",
        "Content-Type: application/json")
    fun putSettingsDevice(
        @Body body : String
    ): Call<BaseResponse<String>>

    @GET("settings/firebase")
    fun getFirebaseSetting(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
        @Query("deviceGuid") deviceGuid: String?,
    ): Call<BaseResponse<FirebaseSetting>>

    @GET("settings/hardware")
    fun getHardwareSetting(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
        @Query("deviceGuid") deviceGuid: String?,
    ): Call<BaseResponse<HardwareSetting>>
}