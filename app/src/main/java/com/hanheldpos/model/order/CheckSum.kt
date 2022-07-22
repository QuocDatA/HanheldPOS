package com.hanheldpos.model.order

import com.hanheldpos.utils.EncryptUtils
import java.io.Serializable
import java.util.*

data class CheckSum(
    val CreateDate: String,

    val Device_id: String,

    val Guid: String = EncryptUtils.getHash(UUID.randomUUID().toString()),
) : Serializable