package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.FeeResp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscountService {
    @GET("discounts/list")
    fun getDiscounts(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
        @Query("locationGuid") language: String? = "em",
    ): Call<DiscountResp>
}