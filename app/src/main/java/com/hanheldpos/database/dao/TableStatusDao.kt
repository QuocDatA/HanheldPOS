package com.hanheldpos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hanheldpos.database.entities.TableStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TableStatusDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(tableStatus: TableStatusEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(objects: List<TableStatusEntity>)

    @Query("SELECT * FROM table_status WHERE id = :id")
    fun get(id: String): TableStatusEntity

    @Query("SELECT * FROM table_status")
    fun getAll(): Flow<MutableList<TableStatusEntity>>

    @Query("DELETE FROM table_status WHERE id = :id")
    fun delete(id : String)

    @Query("DELETE FROM table_status")
    fun deleteAll()
}