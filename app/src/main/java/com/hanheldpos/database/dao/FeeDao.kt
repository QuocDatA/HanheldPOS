package com.hanheldpos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hanheldpos.database.entities.FeeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fee: FeeEntity)

    @Query("SELECT * FROM fee WHERE id = :id")
    fun get(id: String): FeeEntity

    @Query("SELECT * FROM fee")
    fun getAll(): Flow<MutableList<FeeEntity>>

    @Query("DELETE FROM fee")
    fun deleteAll()

}