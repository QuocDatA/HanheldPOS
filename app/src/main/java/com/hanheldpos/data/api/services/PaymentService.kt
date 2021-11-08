package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.payment.PaymentsResp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PaymentService {
    @GET("payments")
    fun getPaymentMethods(
        @Query("userGuid") userGuid: String?,
    ): Call<PaymentsResp>
}