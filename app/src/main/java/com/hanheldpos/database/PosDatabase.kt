package com.hanheldpos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hanheldpos.database.dao.DeviceCodeDao
import com.hanheldpos.database.entities.DeviceCodeEntity


@Database(entities = [ DeviceCodeEntity::class, ], version = 1, exportSchema = false)
public abstract class PosDatabase : RoomDatabase() {

    abstract fun deviceCode() : DeviceCodeDao

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