package com.hanheldpos.database.repo

import com.hanheldpos.database.dao.FloorDao
import com.hanheldpos.database.entities.FloorEntity
import kotlinx.coroutines.flow.Flow

class FloorLocalRepo(private val floorDao: FloorDao) {

    fun insert(floorEntity: FloorEntity) = floorDao.insert(floorEntity);

    fun deleteAll() = floorDao.deleteAll();

    fun get(id : String) : FloorEntity = floorDao.get(id);

    fun getAll(): Flow<MutableList<FloorEntity>> = floorDao.getAll();

}