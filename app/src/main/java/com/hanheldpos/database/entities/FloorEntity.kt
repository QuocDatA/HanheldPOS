package com.hanheldpos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "floor")
data class FloorEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "floor_json")
    val floorJson: String,
): BaseEntity() {
}