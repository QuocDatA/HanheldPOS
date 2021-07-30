package com.diadiem.pos_config

import android.content.Context
import com.google.gson.Gson

object AppConfig {
    var appConfigModel: AppConfigModel? = null

    fun initConfig(context: Context, fileName: String): AppConfigModel? {
        IOJsonFile.getJsonFromAssets(context, fileName)?.also {
//            ApiLogger().log(it)
            appConfigModel = Gson().fromJson(it, AppConfigModel::class.java)
        }

        return appConfigModel
    }


    fun getBaseDomainAPI() = appConfigModel?.baseDomainApi
    fun getEnvironmentKey() = appConfigModel?.environmentKey
}