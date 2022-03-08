package com.hanheldpos.database.repo

import com.hanheldpos.database.dao.OrderCompletedDao
import com.hanheldpos.database.entities.OrderCompletedEntity
import com.hanheldpos.utils.time.DateTimeHelper
import kotlinx.coroutines.flow.Flow
import java.util.*

class OrderCompletedLocalRepo(private val orderCompletedDao: OrderCompletedDao) {

    fun insert(orderCompletedEntity: OrderCompletedEntity) =
        orderCompletedDao.insert(orderCompletedEntity);

    fun insertAll(ordersCompleted: List<OrderCompletedEntity>) =
        orderCompletedDao.insertAll(ordersCompleted);

    fun update(orderCompletedEntity: OrderCompletedEntity) =
        orderCompletedDao.update(orderCompletedEntity.apply {
            modifierAt =
                DateTimeHelper.dateToString(Date(), DateTimeHelper.Format.FULL_DATE_UTC_TIMEZONE)
        });

    fun delete(id: String) = orderCompletedDao.delete(id)

    fun deleteAll() = orderCompletedDao.deleteAll();

    fun get(id: String): OrderCompletedEntity? = orderCompletedDao.get(id);

    fun getAll(): Flow<MutableList<OrderCompletedEntity>> = orderCompletedDao.getAll();

}