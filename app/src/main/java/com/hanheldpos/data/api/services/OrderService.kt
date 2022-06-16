package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.order.filter.OrderFilterResp
import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.order.status.OrderStatusResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.model.order.OrderSummaryPrimary
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

    @GET("order/sync/filter")
    fun getOrderFilter(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
        @Query("deviceGuid") deviceGuid: String?,
        @Query("diningOptionId") diningOptionId: Int?,
        @Query("pageNo") pageNo: Int?,
        @Query("pageSize") pageSize: Int?,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?,
        @Query("orderCode") orderCode: String?,
    ): Call<BaseResponse<OrderFilterResp>>

    @GET("order/history/v3")
    fun getOrderHistory(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
        @Query("deviceGuid") deviceGuid: String?,
        @Query("pageSize") pageSize: Int?,
        @Query("order_id") lastOrderId: String?,
    ): Call<BaseResponse<List<OrderSummaryPrimary>>>

    @GET("order/detail")
    fun getOrderDetail(
        @Query("order_id") orderId: String?,
    ): Call<BaseResponse<OrderModel>>
}