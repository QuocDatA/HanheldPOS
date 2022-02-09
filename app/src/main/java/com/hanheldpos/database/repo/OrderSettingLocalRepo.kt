package com.hanheldpos.database.repo

import com.hanheldpos.database.dao.OrderSettingDao
import com.hanheldpos.database.entities.OrderSettingEntity
import kotlinx.coroutines.flow.Flow

class OrderSettingLocalRepo(private val orderSettingDao: OrderSettingDao) {

    fun insert(orderSettingEntity: OrderSettingEntity) = orderSettingDao.insert(orderSettingEntity);

    fun deleteAll() = orderSettingDao.deleteAll();

    fun get(id : String) : OrderSettingEntity = orderSettingDao.get(id);

    fun getAll(): Flow<MutableList<OrderSettingEntity>> = orderSettingDao.getAll();

}