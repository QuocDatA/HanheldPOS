package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RefundReport(
val RefundType : Int?,
val RefundTypeName : String,
val Quantity : Long?,
val RefundAmount :Double?,
) : Parcelable