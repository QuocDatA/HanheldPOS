package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.order.menu.OrderMenuResp
import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.setting.DeviceCodeResp
import com.hanheldpos.data.api.pojo.table.TableResp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SettingService {

    @GET("Device")
    fun getDataByDeviceCode(
        @Query("AppCode") appCode: String?
    ): Call<DeviceCodeResp>

    @GET("menus/v2")
    fun getOrderMenu(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
    ): Call<OrderMenuResp>

    @GET("order/settings")
    fun getOrderSettings(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
    ): Call<OrderSettingResp>

    @GET("pos/floors/v2")
    fun getPosFloor(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
    ): Call<TableResp>



}