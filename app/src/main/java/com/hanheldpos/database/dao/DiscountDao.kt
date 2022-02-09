package com.hanheldpos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hanheldpos.database.entities.DiscountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiscountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(discount: DiscountEntity)

    @Query("SELECT * FROM discount WHERE id = :id")
    fun get(id: String): DiscountEntity

    @Query("SELECT * FROM discount")
    fun getAll(): Flow<MutableList<DiscountEntity>>

    @Query("DELETE FROM discount")
    fun deleteAll()

}