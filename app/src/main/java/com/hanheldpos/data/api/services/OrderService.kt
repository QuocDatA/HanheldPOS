package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.order.status.OrderStatusResp
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OrderService {
    @GET("order/settings")
    fun getOrderSettings(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
    ): Call<BaseResponse<List<OrderSettingResp>>>

    @GET("ordersStatus/list")
    fun getOrderStatus(
        @Query("userGuid") userGuid: String?,
    ): Call<BaseResponse<List<OrderStatusResp>>>


}