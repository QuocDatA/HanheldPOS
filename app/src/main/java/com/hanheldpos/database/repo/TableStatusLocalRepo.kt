package com.hanheldpos.database.repo

import com.hanheldpos.database.dao.OrderCompletedDao
import com.hanheldpos.database.dao.TableStatusDao
import com.hanheldpos.database.entities.OrderCompletedEntity
import com.hanheldpos.database.entities.TableStatusEntity
import kotlinx.coroutines.flow.Flow

class TableStatusLocalRepo(private val tableStatusDao: TableStatusDao) {

    fun insert(tableStatusEntity: TableStatusEntity) =
        tableStatusDao.insert(tableStatusEntity);

    fun insertAll(tableStatuses: List<TableStatusEntity>) =
        tableStatusDao.insertAll(tableStatuses);

    fun delete(id: String) = tableStatusDao.delete(id)

    fun deleteAll() = tableStatusDao.deleteAll();

    fun get(id: String): TableStatusEntity = tableStatusDao.get(id);

    fun getAll(): Flow<MutableList<TableStatusEntity>> = tableStatusDao.getAll();

}