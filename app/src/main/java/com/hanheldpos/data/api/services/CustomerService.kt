package com.hanheldpos.data.api.services

import com.hanheldpos.data.api.pojo.customer.CustomerSearchResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CustomerService {
    @GET("Customers/search")
    fun getCustomerFromSearch(
        @Query("userGuid") userGuid: String?,
        @Query("keyword") location: String?,
        @Query("pageNo") pageNo : Int? = 1,
        @Query("locationGuid") language: String? = "en",
    ): Call<CustomerSearchResp>
}