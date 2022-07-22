package com.hanheldpos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hanheldpos.database.dao.OrderCompletedDao
import com.hanheldpos.database.dao.TableStatusDao
import com.hanheldpos.database.entities.OrderCompletedEntity
import com.hanheldpos.database.entities.TableStatusEntity


@Database(entities = [ OrderCompletedEntity::class,TableStatusEntity::class ], version = 1, exportSchema = false)
abstract class PosDatabase : RoomDatabase() {

    abstract fun orderCompleted() : OrderCompletedDao

    abstract fun tableStatus() : TableStatusDao

    companion object {
        @Volatile
        private var INSTANCE: PosDatabase? = null

        fun getDatabase(context: Context): PosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PosDatabase::class.java,
                    "pos_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}