package com.hanheldpos.model.menu.orders

import java.util.*

data class SyncedOrdersFilterData(
    var startDay: Date?,
    var endDay: Date?,
    var isAllDay: Boolean,
    var startTime: String?,
    var endTime: String?,
)