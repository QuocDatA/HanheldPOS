package com.hanheldpos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hanheldpos.database.entities.MenuEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(menu: MenuEntity)

    @Query("SELECT * FROM menu WHERE id = :id")
    fun get(id: String): MenuEntity

    @Query("SELECT * FROM menu")
    fun getAll(): Flow<MutableList<MenuEntity>>

    @Query("DELETE FROM menu")
    fun deleteAll()

}