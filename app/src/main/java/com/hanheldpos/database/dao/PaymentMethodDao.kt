package com.hanheldpos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hanheldpos.database.entities.PaymentMethodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentMethodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(paymentMethod: PaymentMethodEntity)

    @Query("SELECT * FROM payment_method WHERE id = :id")
    fun get(id: String): PaymentMethodEntity

    @Query("SELECT * FROM payment_method")
    fun getAll(): Flow<MutableList<PaymentMethodEntity>>

    @Query("DELETE FROM payment_method")
    fun deleteAll()

}