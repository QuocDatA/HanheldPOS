package com.hanheldpos.data.api.pojo.cashdrawer.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReportCashDrawerResp(
    val CashDrawerGuid: String?,
    val Reports: List<Report>?,
    val Title: String?
) : Parcelable