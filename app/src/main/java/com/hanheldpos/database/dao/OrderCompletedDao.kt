package com.hanheldpos.database.dao

import androidx.room.*
import com.hanheldpos.database.entities.OrderCompletedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderCompletedDao {

    @Insert
    fun insert(orderCompleted: OrderCompletedEntity)

    @Insert
    fun insertAll(objects: List<OrderCompletedEntity>)

    @Update
    fun update(orderCompleted: OrderCompletedEntity)

    @Query("SELECT * FROM order_completed WHERE id = :id")
    fun get(id: String?): OrderCompletedEntity?

    @Query("SELECT * FROM order_completed")
    fun getAllLiveData(): Flow<MutableList<OrderCompletedEntity>>

    @Query("SELECT * FROM order_completed")
    fun getAll(): MutableList<OrderCompletedEntity>

    @Query("DELETE FROM order_completed WHERE id = :id")
    fun delete(id : String)

    @Query("DELETE FROM order_completed")
    fun deleteAll()

}