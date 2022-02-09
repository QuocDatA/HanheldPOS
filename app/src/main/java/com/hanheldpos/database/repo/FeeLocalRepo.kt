package com.hanheldpos.database.repo

import com.hanheldpos.database.dao.FeeDao
import com.hanheldpos.database.entities.FeeEntity
import kotlinx.coroutines.flow.Flow

class FeeLocalRepo(private val feeDao: FeeDao) {

    fun insert(feeEntity: FeeEntity) = feeDao.insert(feeEntity);

    fun deleteAll() = feeDao.deleteAll();

    fun get(id : String) : FeeEntity = feeDao.get(id);

    fun getAll(): Flow<MutableList<FeeEntity>> = feeDao.getAll();

}