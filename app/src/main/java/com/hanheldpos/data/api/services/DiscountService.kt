package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.discount.CouponResp
import com.hanheldpos.data.api.pojo.discount.DiscountCoupon
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.*

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

    @POST("cart/discount/only/v2")
    @Headers("Accept: text/plain",
        "Content-Type: application/json")
    fun postDiscountCoupon(
        @Body body: String,
    ): Call<BaseResponse<List<DiscountCoupon>>?>
}