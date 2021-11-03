package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.order.menu.OrderMenuResp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MenuService {
    @GET("menus/v2")
    fun getOrderMenu(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
    ): Call<OrderMenuResp>
}