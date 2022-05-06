package com.hanheldpos.data.api.pojo.report

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Report(
    val Date: String,
    val Employee: String,
    val LocationId: Int,
    val LocationName: String
) : Parcelable