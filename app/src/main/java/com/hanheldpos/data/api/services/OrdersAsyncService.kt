package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.setting.SettingDeviceResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.model.order.OrderSubmitResp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

interface OrdersAsyncService {
    @POST("orders/v3")
    @Headers("Accept: text/plain",
        "Content-Type: application/json")
    fun postOrderSubmit(
        @Body body : String
    ): Call<BaseResponse<OrderSubmitResp>>
}