package com.hanheldpos.database.repo

import com.hanheldpos.database.dao.DeviceCodeDao
import com.hanheldpos.database.entities.DeviceCodeEntity
import kotlinx.coroutines.flow.Flow

class DeviceCodeLocalRepo(private val deviceDao: DeviceCodeDao) {

    fun insert(deviceCodeEntity: DeviceCodeEntity) = deviceDao.insert(deviceCodeEntity);

    fun deleteAll() = deviceDao.deleteAll();

    fun get(id : String) : DeviceCodeEntity = deviceDao.get(id);

    fun getAll(): Flow<MutableList<DeviceCodeEntity>> = deviceDao.getAll();

}