package com.hanheldpos.data.repository

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
open class BasePagingResponse<T> : BaseResponse<T>(), Parcelable {
    @SerializedName("page_size")
    val pageSize: Int = 0

    @SerializedName("page_no")
    val pageNo: Int = 0

    @SerializedName("total_page")
    val totalPage: Int = 0
}
