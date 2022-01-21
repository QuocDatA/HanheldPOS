package com.hanheldpos.database.entities

import com.hanheldpos.utils.GSonUtils
import kotlin.reflect.KClass

open class BaseEntity {
    fun <T: BaseEntity> parseStringToObject(value: String, type: KClass<T>): T? {
        return GSonUtils.gson.fromJson(value, type.java)
    }
}