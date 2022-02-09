package com.hanheldpos.database.repo

import com.hanheldpos.database.dao.DiscountDao
import com.hanheldpos.database.entities.DiscountEntity
import kotlinx.coroutines.flow.Flow

class DiscountLocalRepo(private val discountDao: DiscountDao) {

    fun insert(discountEntity: DiscountEntity) = discountDao.insert(discountEntity);

    fun deleteAll() = discountDao.deleteAll();

    fun get(id : String) : DiscountEntity = discountDao.get(id);

    fun getAll(): Flow<MutableList<DiscountEntity>> = discountDao.getAll();

}