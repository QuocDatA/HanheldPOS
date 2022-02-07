package com.hanheldpos.model.report

import java.util.*

data class SaleReportCustomData(
    val startDay: Date?,
    val endDay: Date?,
    val isAllDay: Boolean,
    val startTime: String,
    val endTime: String,
    val isAllDevice: Boolean,
    val isCurrentDrawer: Boolean
) {
}