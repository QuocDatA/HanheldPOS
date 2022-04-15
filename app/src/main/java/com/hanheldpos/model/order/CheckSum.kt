package com.hanheldpos.model.order

import java.io.Serializable
import java.util.*

data class CheckSum(
    val CreateDate: String,

    val Device_id : String,

    val Guid: String = UUID.randomUUID().toString().format("N"),
) : Serializable {
}