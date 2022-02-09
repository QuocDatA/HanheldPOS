package com.hanheldpos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hanheldpos.database.entities.OrderCompletedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderCompletedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(orderCompleted: OrderCompletedEntity)

    @Query("SELECT * FROM order_completed WHERE id = :id")
    fun get(id: String): OrderCompletedEntity

    @Query("SELECT * FROM order_completed")
    fun getAll(): Flow<MutableList<OrderCompletedEntity>>

    @Query("DELETE FROM order_completed")
    fun deleteAll()

}