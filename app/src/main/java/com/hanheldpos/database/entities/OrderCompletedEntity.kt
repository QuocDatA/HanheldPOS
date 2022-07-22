package com.hanheldpos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hanheldpos.model.order.OrderStatus

@Entity (tableName = "order_completed")
data class OrderCompletedEntity(
    @PrimaryKey
    val id : String,
    @ColumnInfo(name = "table_id")
    var tableId : String?,
    @ColumnInfo(name = "order_details_json")
    var orderDetailsJson : String?,
    @ColumnInfo(name = "order_json")
    val orderJson: String?,
    @ColumnInfo(name = "is_sync", defaultValue = "false")
    var isSync : Boolean? = false,
    @ColumnInfo(name = "drawer_id")
    val drawerId : String?,
    @ColumnInfo(name = "status_id")
    val statusId : OrderStatus?,
    @ColumnInfo(name = "create_at" , defaultValue = "CURRENT_TIMESTAMP")
    val createAt : String?,
    @ColumnInfo(name = "modifier_at" , defaultValue = "CURRENT_TIMESTAMP")
    var modifierAt : String?,
    @ColumnInfo(name = "request_log_json" , defaultValue = "")
    var requestLogJson : String? = null,
): BaseEntity()