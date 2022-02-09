package com.hanheldpos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hanheldpos.database.entities.DiscountDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiscountDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(discountDetail: DiscountDetailEntity)

    @Query("SELECT * FROM discount_detail WHERE id = :id")
    fun get(id: String): DiscountDetailEntity

    @Query("SELECT * FROM discount_detail")
    fun getAll(): Flow<MutableList<DiscountDetailEntity>>

    @Query("DELETE FROM discount_detail")
    fun deleteAll()

}