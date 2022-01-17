package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.order.menu.MenuResp
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MenuService {
    @GET("menus/v3")
    fun getOrderMenu(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
    ): Call<BaseResponse<MenuResp>>
}