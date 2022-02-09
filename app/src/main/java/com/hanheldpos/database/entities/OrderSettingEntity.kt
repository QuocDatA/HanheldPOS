package com.hanheldpos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_setting")
data class OrderSettingEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "order_setting_json")
    val orderSettingJson: String,
): BaseEntity() {
}