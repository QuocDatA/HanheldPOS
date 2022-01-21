package com.hanheldpos.data.api.pojo.customer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomerSearchResp(
    val List: List<CustomerResp>,
    val PageNo: Int,
    val PageSize: Int,
    val Total: Int,
    val TotalPage: Int
) : Parcelable