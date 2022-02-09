package com.hanheldpos.database.repo

import com.hanheldpos.database.dao.DiscountDetailDao
import com.hanheldpos.database.entities.DiscountDetailEntity
import kotlinx.coroutines.flow.Flow

class DiscountDetailLocalRepo(private val discountDetailDao: DiscountDetailDao) {

    fun insert(discountDetailEntity: DiscountDetailEntity) = discountDetailDao.insert(discountDetailEntity);

    fun deleteAll() = discountDetailDao.deleteAll();

    fun get(id : String) : DiscountDetailEntity = discountDetailDao.get(id);

    fun getAll(): Flow<MutableList<DiscountDetailEntity>> = discountDetailDao.getAll();

}