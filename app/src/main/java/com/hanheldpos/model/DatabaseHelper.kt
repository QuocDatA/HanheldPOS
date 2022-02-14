package com.hanheldpos.model

import android.content.Context
import com.hanheldpos.database.PosDatabase
import com.hanheldpos.database.repo.DeviceCodeLocalRepo

object DatabaseHelper {
    private lateinit var posDatabase: PosDatabase;

    fun initDatabase(context: Context) {
        posDatabase = PosDatabase.getDatabase(context);
    }

    val deviceCodeLocalRepo: DeviceCodeLocalRepo? = null
        get() {
            return field ?: DeviceCodeLocalRepo(posDatabase.deviceCode())
        }


}