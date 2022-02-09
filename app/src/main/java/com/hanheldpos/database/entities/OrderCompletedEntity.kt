package com.hanheldpos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "order_completed")
data class OrderCompletedEntity(
    @PrimaryKey
    val id : String,
    @ColumnInfo(name = "order_completed_json")
    val orderCompletedJson: String,
): BaseEntity() {
}