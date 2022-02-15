package com.hanheldpos.database.repo

import com.hanheldpos.database.dao.OrderCompletedDao
import com.hanheldpos.database.entities.OrderCompletedEntity
import kotlinx.coroutines.flow.Flow

class OrderCompletedLocalRepo(private val orderCompletedDao: OrderCompletedDao) {

    fun insert(orderCompletedEntity: OrderCompletedEntity) = orderCompletedDao.insert(orderCompletedEntity);

    fun deleteAll() = orderCompletedDao.deleteAll();

    fun get(id : String) : OrderCompletedEntity = orderCompletedDao.get(id);

    fun getAll(): MutableList<OrderCompletedEntity> = orderCompletedDao.getAll();

}