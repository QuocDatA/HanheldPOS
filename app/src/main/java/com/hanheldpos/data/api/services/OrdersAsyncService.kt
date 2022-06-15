package com.hanheldpos.data.api.services

import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.model.order.OrderSubmitResp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OrdersAsyncService {
    @POST("orders/v3")
    @Headers("Accept: text/plain",
        "Content-Type: application/json")
    fun postOrderSubmit(
        @Body body : String
    ): Call<BaseResponse<OrderSubmitResp>>
}