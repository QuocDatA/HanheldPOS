package com.hanheldpos.data.api.services.employee

import com.hanheldpos.data.api.pojo.employee.EmployeeResp
import com.hanheldpos.data.repository.GDataResp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EmployeeService {
    @GET("Employee")
    fun getDataEmployee(
        @Query("userGuid") userGuid: String,
        @Query("passCode") passCode: String,
        @Query("locationGuid") locationGuid: String,
    ) : Call<GDataResp<EmployeeResp>>
}