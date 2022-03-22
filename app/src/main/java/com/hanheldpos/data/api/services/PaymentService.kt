package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.payment.GiftCardResp
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.payment.WalletCardResp
import com.hanheldpos.data.repository.BaseResponse
import kotlinx.parcelize.RawValue
import retrofit2.Call
import retrofit2.http.*

interface PaymentService {
    @GET("payments")
    fun getPaymentMethods(
        @Query("userGuid") userGuid: String?,
    ): Call<BaseResponse<List<PaymentMethodResp>>>

    @GET("payments/v3/GiftCard")
    fun getValidGiftCard(
        @Query("userGuid") userGuid: String?,
        @Query("codeCard") codeCard: String?
    ): Call<BaseResponse<GiftCardResp>>

    @GET("payments/v3")
    fun getValidWallet(
        @Query("_key") keyWallet: String?
    ): Call<BaseResponse<WalletCardResp>>

    @POST("payments/v3")
    @Headers("Accept: text/plain",
        "Content-Type: application/json")
    fun syncPayment(
        @Body body : String?
    ) : Call<BaseResponse<@RawValue Any>>
}