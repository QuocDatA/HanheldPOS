package com.hanheldpos.data.api

import com.diadiem.pos_config.AppConfig
import com.utils.constants.Const

object ApiConst {

    const val PHOTO_DIR = "photo/"
    const val VISIBLE = 1
    const val IN_VISIBLE = 0
    // Url
    private val BASE_DEV_URL = AppConfig.getBaseDomainAPI();
    private  val BASE_PROD_URL = AppConfig.getBaseDomainAPI();
    val BASE_URL = if (Const.DEBUG_MODE) BASE_DEV_URL else BASE_PROD_URL

    const val BASE_ASYNC_URL = "https://lin-dev-fro-async-api-1.diadiem.vn/api/"

    object Location {
        const val ALL = "ALL"
        const val NONE = "NONE"
        const val COUNTRY_ID = 704
    }
}
