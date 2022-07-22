package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.cashdrawer.CashDrawerStatusResp
import com.hanheldpos.data.api.pojo.cashdrawer.CreateCashDrawerResp
import com.hanheldpos.data.api.pojo.cashdrawer.pay_in_out.PaidInOutListResp
import com.hanheldpos.data.api.pojo.cashdrawer.pay_in_out.PayInOutResp
import com.hanheldpos.data.api.pojo.cashdrawer.report.ReportCashDrawerResp
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CashDrawerService {
    @POST("Orders/CashDrawer/check")
    @Headers("Accept: text/plain",
        "Content-Type: application/json")
    fun getStatusCashDrawer(
        @Body body: String,
    ): Call<BaseResponse<List<CashDrawerStatusResp>>?>

    @POST("Orders/CashDrawer")
    @Headers("Accept: text/plain",
        "Content-Type: application/json")
    fun postCreateCashDrawer(
        @Body body: String,
    ): Call<BaseResponse<List<CreateCashDrawerResp>>?>

    @POST("Orders/CashDrawer/Report")
    @Headers("Accept: text/plain",
        "Content-Type: application/json")
    fun getReportCurrentDrawer(
        @Body body: String,
    ): Call<BaseResponse<List<ReportCashDrawerResp>>?>

    @POST("Orders/CashDrawer/Paid/InOut/Lists")
    @Headers("Accept: text/plain",
        "Content-Type: application/json")
    fun getListPaidInOut(
        @Body body: String,
    ) : Call<BaseResponse<List<PaidInOutListResp>>?>

    @POST("Orders/CashDrawer/Paid/InOut")
    @Headers("Accept: text/plain",
        "Content-Type: application/json")
    fun postPaidInOut(
        @Body body: String,
    ) : Call<BaseResponse<List<PayInOutResp>>?>
}