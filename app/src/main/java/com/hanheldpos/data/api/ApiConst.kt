package com.hanheldpos.data.api

import com.diadiem.pos_config.AppConfig
import com.utils.constants.Const

object ApiConst {

    // Url
    private val BASE_DEV_URL = AppConfig.getBaseDomainAPI();
    private const val BASE_PROD_URL = "/"
    val BASE_URL = if (Const.DEBUG_MODE) BASE_DEV_URL else BASE_PROD_URL

}
