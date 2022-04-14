package com.hanheldpos.database

import com.hanheldpos.data.api.pojo.floor.FloorTable
import com.hanheldpos.database.entities.*
import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.model.order.OrderStatus
import com.hanheldpos.utils.GSonUtils
import com.hanheldpos.utils.DateTimeUtils
import java.util.*

object DatabaseMapper {
    fun mappingOrderCompletedReqToEntity(orderReq: OrderReq): OrderCompletedEntity {
        return OrderCompletedEntity(
            id = orderReq.Order.Code!!,
            orderReq.OrderSummary.TableId,
            orderDetailsJson = GSonUtils.toJson(orderReq),
            orderJson = GSonUtils.toJson(orderReq.Order),
            false,
            orderReq.Order.CashDrawer_id!!,
            statusId = OrderStatus.fromInt(orderReq.Order.OrderStatusId!!)!!,
            createAt = orderReq.Order.CreateDate!!,
            modifierAt = DateTimeUtils.dateToString(Date(), DateTimeUtils.Format.FULL_DATE_UTC_TIMEZONE),
        )
    }

    fun mappingOrderReqFromEntity(orderCompletedEntity: OrderCompletedEntity): OrderReq {
        return GSonUtils.toObject<OrderReq>(orderCompletedEntity.orderDetailsJson)!!;
    }

    fun mappingTableToEntity(table: FloorTable): TableStatusEntity {
        return TableStatusEntity(
            id = table._Id,
            tableStatusJson = GSonUtils.toJson(table)
        )
    }

    fun mappingTableFromEntity(tableStatusEntity: TableStatusEntity): FloorTable {
        return GSonUtils.toObject<FloorTable>(tableStatusEntity.tableStatusJson)!!
    }
}