package com.hanheldpos.database.repo

import com.hanheldpos.database.dao.OrderCompletedDao
import com.hanheldpos.database.entities.OrderCompletedEntity
import com.hanheldpos.utils.DateTimeUtils
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
                DateTimeUtils.dateToString(Date(), DateTimeUtils.Format.YYYY_MM_DD_HH_MM_SS)
        });

    fun delete(id: String) = orderCompletedDao.delete(id)

    fun deleteAll() = orderCompletedDao.deleteAll();

    fun get(id: String?): OrderCompletedEntity? = orderCompletedDao.get(id);

    fun getAllLiveData(): Flow<MutableList<OrderCompletedEntity>> = orderCompletedDao.getAllLiveData()

    fun getAll(): MutableList<OrderCompletedEntity> = orderCompletedDao.getAll()



}