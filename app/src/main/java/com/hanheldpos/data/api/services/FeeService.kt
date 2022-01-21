package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.fee.FeeResp
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FeeService {
    @GET("fees/v3")
    fun getFees(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
    ): Call<BaseResponse<FeeResp>>
}