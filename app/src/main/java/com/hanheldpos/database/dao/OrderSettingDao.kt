package com.hanheldpos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hanheldpos.database.entities.OrderSettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderSettingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(orderSetting: OrderSettingEntity)

    @Query("SELECT * FROM order_setting WHERE id = :id")
    fun get(id: String): OrderSettingEntity

    @Query("SELECT * FROM order_setting")
    fun getAll(): Flow<MutableList<OrderSettingEntity>>

    @Query("DELETE FROM order_setting")
    fun deleteAll()

}