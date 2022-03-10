package com.hanheldpos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_status")
data class TableStatusEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "table_status_json")
    val tableStatusJson: String,
) :BaseEntity()