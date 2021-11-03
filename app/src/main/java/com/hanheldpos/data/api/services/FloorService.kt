package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.table.TableResp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FloorService {
    @GET("pos/floors/v2")
    fun getPosFloor(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
    ): Call<TableResp>
}