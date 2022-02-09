package com.hanheldpos.database.dao

import androidx.room.*
import com.hanheldpos.database.entities.DeviceCodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceCodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deviceCode: DeviceCodeEntity)

    @Query("SELECT * FROM device_code WHERE id = :id")
    fun get(id: String): DeviceCodeEntity

    @Query("SELECT * FROM device_code")
    fun getAll(): MutableList<DeviceCodeEntity>

    @Query("DELETE FROM device_code")
    fun deleteAll()

}