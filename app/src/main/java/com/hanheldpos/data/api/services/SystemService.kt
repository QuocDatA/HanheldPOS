package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.setting.SettingDeviceResp
import com.hanheldpos.data.api.pojo.system.AddressTypeResp
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT

interface SystemService {
    @GET("system/address-type")
    @Headers("Accept: text/plain",
        "Content-Type: application/json")
    fun getSystemAddressType(
    ): Call<BaseResponse<List<AddressTypeResp>>>
}