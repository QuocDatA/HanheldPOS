package com.hanheldpos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hanheldpos.model.order.OrderStatus
import java.util.*

@Entity (tableName = "order_completed")
data class OrderCompletedEntity(
    @PrimaryKey
    val id : String,
    @ColumnInfo(name = "table_id")
    var tableId : String,
    @ColumnInfo(name = "order_completed_json")
    val orderCompletedJson: String,
    @ColumnInfo(name = "is_sync", defaultValue = "false")
    var isSync : Boolean ,
    @ColumnInfo(name = "drawer_id")
    val drawerId : String,
    @ColumnInfo(name = "status_id")
    val statusId : OrderStatus,
    @ColumnInfo(name = "create_at" , defaultValue = "CURRENT_TIMESTAMP")
    val createAt : String,
    @ColumnInfo(name = "modifier_at" , defaultValue = "CURRENT_TIMESTAMP")
    var modifierAt : String,
): BaseEntity() {
}