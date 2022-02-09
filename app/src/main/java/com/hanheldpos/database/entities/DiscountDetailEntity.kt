package com.hanheldpos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "discount_detail")
data class DiscountDetailEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "discount_detail_json")
    val discountDetailJson: String,
): BaseEntity() {
}