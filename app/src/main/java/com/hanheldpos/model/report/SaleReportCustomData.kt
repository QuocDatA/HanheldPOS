package com.hanheldpos.model.report

import java.util.*

data class SaleReportCustomData(
    var startDay: Date?,
    var endDay: Date?,
    var isAllDay: Boolean,
    var startTime: String?,
    var endTime: String?,
    var isAllDevice: Boolean,
    var isCurrentDrawer: Boolean
) {
}