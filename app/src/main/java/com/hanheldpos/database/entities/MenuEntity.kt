package com.hanheldpos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "menu")
data class MenuEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "menu_json")
    val menu_Json: String
): BaseEntity() {
}