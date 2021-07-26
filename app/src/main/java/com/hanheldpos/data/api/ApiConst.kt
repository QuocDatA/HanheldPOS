package com.hanheldpos.data.api

import com.utils.constants.Const

object ApiConst {

    // Url
    private const val BASE_DEV_URL = "/"
    private const val BASE_PROD_URL = "/"
    val BASE_URL = if (Const.DEBUG_MODE) BASE_DEV_URL else BASE_PROD_URL

}
