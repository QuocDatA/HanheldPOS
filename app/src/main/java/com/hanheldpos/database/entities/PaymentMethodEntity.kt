package com.hanheldpos.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_method")
data class PaymentMethodEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "payment_method_json")
    val paymentMethodJson: String,
): BaseEntity() {
}