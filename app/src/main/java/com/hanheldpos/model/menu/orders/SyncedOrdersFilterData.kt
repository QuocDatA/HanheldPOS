package com.hanheldpos.model.menu.orders

import com.hanheldpos.data.api.pojo.order.settings.DiningOption
import java.util.*

data class SyncedOrdersFilterData(
    var startDay: Date?,
    var endDay: Date?,
    var isAllDay: Boolean,
    var startTime: String?,
    var endTime: String?,
    val diningOption: DiningOption?,
)