package com.hanheldpos.model

import android.content.Context
import com.hanheldpos.database.PosDatabase

object DatabaseHelper {
    private lateinit var posDatabase: PosDatabase;

    fun initDatabase(context: Context) {
        posDatabase = PosDatabase.getDatabase(context);
    }


}