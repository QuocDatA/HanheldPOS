package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.cashdrawer.CashDrawerStatusResp
import com.hanheldpos.data.api.pojo.cashdrawer.CreateCashDrawer
import com.hanheldpos.data.api.pojo.cashdrawer.CreateCashDrawerResp
import com.hanheldpos.data.api.pojo.customer.CustomerSearchResp
import com.hanheldpos.model.cashdrawer.CreateCashDrawerReq
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface CashDrawerService {
    @POST("Orders/CashDrawer/check")
    @Headers("Accept: text/plain",
        "Content-Type: application/json")
    fun getStatusCashDrawer(
        @Body body: String,
    ): Call<CashDrawerStatusResp>

    @POST("Orders/CashDrawer")
    @Headers("Accept: text/plain",
        "Content-Type: application/json")
    fun postCreateCashDrawer(
        @Body body: String,
    ): Call<CreateCashDrawerResp>
}