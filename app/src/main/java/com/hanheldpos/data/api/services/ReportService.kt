package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportService {
    @GET("reports/order/daily/v4")
    fun getSalesReport(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") locationGuid: String?,
        @Query("deviceGuid") deviceGuid: String?,
        @Query("employeeGuid") employeeGuid: String?,
        @Query("day") day: String?,
        @Query("startHour") startHour: String?,
        @Query("cashDrawerGuid") cashDrawerGuid: String?,
        @Query("endHour") endHour: String?,
        @Query("isAllDevice") isAllDevice: Boolean? = false,
        @Query("isCurrentCashdrawer") isCurrentCashdrawer: Boolean? = false,
    ): Call<BaseResponse<ReportSalesResp>>
}