package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.loyalty.LoyaltyResp
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoyaltyPointService {
    @POST("cart/v3/loyalty")
    @Headers("Accept: text/plain",
        "Content-Type: application/json")
    fun postCustomerLoyaltyPoint(
        @Body body: String,
    ): Call<BaseResponse<LoyaltyResp>>
}