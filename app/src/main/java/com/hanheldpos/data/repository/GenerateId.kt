package com.hanheldpos.data.repository

import java.util.*

object GenerateId {

    fun getExtraId(id: Int) = "Extra/$id"

    fun getStaffId(id: Int) = "Staff/$id"

    fun getOrderItemId() = UUID.randomUUID().toString()
}