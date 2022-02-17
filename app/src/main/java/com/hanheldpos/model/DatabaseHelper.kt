package com.hanheldpos.model

import android.content.Context
import com.hanheldpos.database.PosDatabase
import com.hanheldpos.database.dao.OrderCompletedDao
import com.hanheldpos.database.repo.OrderCompletedLocalRepo

object DatabaseHelper {
    private lateinit var posDatabase: PosDatabase;

    fun initDatabase(context: Context) {
        posDatabase = PosDatabase.getDatabase(context);
    }

    val ordersCompleted: OrderCompletedLocalRepo
        get() {
            return OrderCompletedLocalRepo(posDatabase.orderCompleted());
        }

}