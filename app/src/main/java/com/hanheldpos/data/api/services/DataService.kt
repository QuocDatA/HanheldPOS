package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.data.DataVersion
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DataService {
    @GET("v2/data/version")
    fun getDataVersion(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
        @Query("deviceGuid") deviceGuid: String?,
    ): Call<BaseResponse<DataVersion>>
}