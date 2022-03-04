package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.discount.CouponResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.FeeResp
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscountService {
    @GET("discounts/list/v3")
    fun getDiscounts(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
        @Query("language") language: String? = "en",
    ): Call<BaseResponse<List<DiscountResp>>>

    @GET("coupon/list/detail")
    fun getDiscountDetails(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
        @Query("language") language: String? = "en",
    ): Call<BaseResponse<List<CouponResp>>>
}