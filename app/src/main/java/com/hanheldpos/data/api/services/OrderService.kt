package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.fee.FeeResp
import com.hanheldpos.data.api.pojo.order.menu.OrderMenuResp
import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.setting.DeviceCodeResp
import com.hanheldpos.data.api.pojo.table.TableResp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OrderService {
    @GET("order/settings")
    fun getOrderSettings(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
    ): Call<OrderSettingResp>

}