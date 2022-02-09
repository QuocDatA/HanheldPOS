package com.hanheldpos.database.repo

import com.hanheldpos.database.dao.PaymentMethodDao
import com.hanheldpos.database.entities.PaymentMethodEntity
import kotlinx.coroutines.flow.Flow

class PaymentMethodLocalRepo(private val paymentMethodDao: PaymentMethodDao) {

    fun insert(paymentMethodEntity: PaymentMethodEntity) = paymentMethodDao.insert(paymentMethodEntity);

    fun deleteAll() = paymentMethodDao.deleteAll();

    fun get(id : String) : PaymentMethodEntity = paymentMethodDao.get(id);

    fun getAll(): Flow<MutableList<PaymentMethodEntity>> = paymentMethodDao.getAll();

}