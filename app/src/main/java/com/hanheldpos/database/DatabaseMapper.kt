package com.hanheldpos.database

import com.hanheldpos.data.api.pojo.device.DeviceCodeResp
import com.hanheldpos.data.api.pojo.discount.CouponResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.FeeResp
import com.hanheldpos.data.api.pojo.floor.FloorResp
import com.hanheldpos.data.api.pojo.floor.FloorTable
import com.hanheldpos.data.api.pojo.order.menu.MenuResp
import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.database.entities.*
import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.utils.GSonUtils

object DatabaseMapper {
    fun mappingOrderCompletedReqToEntity(orderReq: OrderReq): OrderCompletedEntity {
        return OrderCompletedEntity(
            id = orderReq.Order.Code!!,
            orderCompletedJson = GSonUtils.toJson(orderReq)
        )
    }

    fun mappingOrderReqFromEntity(orderCompletedEntity: OrderCompletedEntity): OrderReq {
        return GSonUtils.toObject<OrderReq>(orderCompletedEntity.orderCompletedJson)!!;
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