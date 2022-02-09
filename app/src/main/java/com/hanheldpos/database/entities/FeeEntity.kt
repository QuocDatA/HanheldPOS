package com.hanheldpos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "fee")
data class FeeEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "fee_json")
    val feeJson: String,
): BaseEntity() {
}