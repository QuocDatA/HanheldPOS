package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.resource.ResourceResp
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ResourceService {
    @GET("pos/resource/list/v2")
    fun getResourceModel(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
    ): Call<BaseResponse<List<ResourceResp>>>
}