package com.hanheldpos.data.api.pojo.cashdrawer.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Report(
    val CharType: Int,
    val Id: Int,
    val OrderNo: Int,
    val Title: String?,
    val Value: @RawValue Any?
) : Parcelable