package com.hanheldpos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hanheldpos.database.entities.FloorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FloorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(floor: FloorEntity)

    @Query("SELECT * FROM floor WHERE id = :id")
    fun get(id: String): FloorEntity

    @Query("SELECT * FROM floor")
    fun getAll(): Flow<MutableList<FloorEntity>>

    @Query("DELETE FROM floor")
    fun deleteAll()

}