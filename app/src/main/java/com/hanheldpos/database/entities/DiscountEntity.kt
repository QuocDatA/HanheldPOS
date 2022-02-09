package com.hanheldpos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "discount")
data class DiscountEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "discount_json")
    val discountJson: String,
): BaseEntity() {
}