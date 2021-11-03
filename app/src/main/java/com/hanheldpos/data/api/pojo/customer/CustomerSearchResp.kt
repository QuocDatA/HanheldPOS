package com.hanheldpos.data.api.pojo.customer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class CustomerSearchResp(
    val DidError: Boolean,
    val ErrorMessage: String?,
    val Message: String?,
    val Model: List<SearchModel>
) : Parcelable {
    @Parcelize
    data class SearchModel(
        val List: List<CustomerResp>,
        val PageNo: Int,
        val PageSize: Int,
        val Total: Int,
        val TotalPage: Int
    ) : Parcelable
}