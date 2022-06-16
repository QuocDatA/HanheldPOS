package com.hanheldpos.database

import com.hanheldpos.data.api.pojo.floor.FloorTable
import com.hanheldpos.database.entities.*
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.model.order.OrderStatus
import com.hanheldpos.utils.GSonUtils
import com.hanheldpos.utils.DateTimeUtils
import java.util.*

object DatabaseMapper {
    fun mappingOrderCompletedReqToEntity(orderModel: OrderModel): OrderCompletedEntity {
        return OrderCompletedEntity(
            id = orderModel.Order.Code!!,
            orderModel.OrderSummary.TableId,
            orderDetailsJson = GSonUtils.toJson(orderModel),
            orderJson = GSonUtils.toJson(orderModel.Order),
            false,
            orderModel.Order.CashDrawer_id!!,
            statusId = OrderStatus.fromInt(orderModel.Order.OrderStatusId!!)!!,
            createAt = orderModel.Order.CreateDate!!,
            modifierAt = DateTimeUtils.dateToString(Date(), DateTimeUtils.Format.YYYY_MM_DD_HH_MM_SS),
        )
    }

    fun mappingOrderReqFromEntity(orderCompletedEntity: OrderCompletedEntity): OrderModel {
        return GSonUtils.toObject<OrderModel>(orderCompletedEntity.orderDetailsJson)!!;
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