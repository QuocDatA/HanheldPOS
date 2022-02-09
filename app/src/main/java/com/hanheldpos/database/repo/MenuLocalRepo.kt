package com.hanheldpos.database.repo

import com.hanheldpos.database.dao.MenuDao
import com.hanheldpos.database.entities.MenuEntity
import kotlinx.coroutines.flow.Flow

class MenuLocalRepo(private val menuDao: MenuDao) {

    fun insert(menuEntity: MenuEntity) = menuDao.insert(menuEntity);

    fun deleteAll() = menuDao.deleteAll();

    fun get(id : String) : MenuEntity = menuDao.get(id);

    fun getAll(): Flow<MutableList<MenuEntity>> = menuDao.getAll();

}