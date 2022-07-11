package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.customer.CustomerActivitiesResp
import com.hanheldpos.data.api.pojo.customer.CustomerProfileResp
import com.hanheldpos.data.api.pojo.customer.CustomerSearchResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CustomerService {
    @GET("Customers/search")
    fun getCustomerFromSearch(
        @Query("userGuid") userGuid: String?,
        @Query("keyword") keyword: String?,
        @Query("pageNo") pageNo : Int? = 1,
        @Query("locationGuid") language: String? = "en",
    ): Call<BaseResponse<List<CustomerSearchResp>>>

    @GET("Customers/activity")
    fun getCustomerActivities(
        @Query("userGuid") userGuid: String?,
        @Query("locationGuid") location: String?,
        @Query("pageNo") pageNo : Int? = 1,
        @Query("pageSize") pageSize : Int? = 10,
        @Query("customerGuid") customerId: String?,
    ): Call<BaseResponse<CustomerActivitiesResp>>

    @GET("Customers/profiles")
    fun getCustomerProfileDetail(
        @Query("userGuid") userGuid: String?,
        @Query("customerGuid") customerGuid: String?,
    ): Call<BaseResponse<CustomerProfileResp>>
}