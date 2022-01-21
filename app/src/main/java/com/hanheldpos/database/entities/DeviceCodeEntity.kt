package com.hanheldpos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_code")
data class DeviceCodeEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "device_code_json")
    val deviceCodeJson: String,
) : BaseEntity() {

}
