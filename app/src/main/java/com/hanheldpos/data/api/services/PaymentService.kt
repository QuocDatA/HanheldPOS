package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PaymentService {
    @GET("payments")
    fun getPaymentMethods(
        @Query("userGuid") userGuid: String?,
    ): Call<BaseResponse<List<PaymentMethodResp>>>
}