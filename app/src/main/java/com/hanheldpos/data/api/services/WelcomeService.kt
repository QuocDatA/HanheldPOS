package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.welcome.WelcomeRespModel
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface WelcomeService {
    @GET("pos/ui")
    @Headers("Accept: text/plain",
        "Content-Type: application/json")
    fun getWelcomeModel(): Call<BaseResponse<List<WelcomeRespModel>>>
}