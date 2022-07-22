package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.floor.FloorResp
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FloorService {
    @GET("pos/floors/v2")
    fun getPosFloor(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
    ): Call<BaseResponse<List<FloorResp>>>
}