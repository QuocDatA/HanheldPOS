package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.receipt.ReceiptCashier
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ReceiptService {
    @GET("Receipts/cashiers")
    fun getReceiptCashier(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") locationGuid: String?,
    ): Call<BaseResponse<List<ReceiptCashier>>>
}