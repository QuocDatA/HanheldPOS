package com.diadiem.pos_config.utils.localization.core

import android.content.Context
import android.content.res.Resources
import java.util.*

//https://github.com/akexorcist/Localization
class LocalizationApplicationDelegate {

    fun onConfigurationChanged(context: Context) =
        LocalizationUtility.applyLocalizationContext(context)

    fun attachBaseContext(context: Context): Context =
        LocalizationUtility.applyLocalizationContext(context)

    fun getApplicationContext(applicationContext: Context): Context =
        LocalizationUtility.applyLocalizationConfig(applicationContext)

    fun getResources(appContext: Context): Resources = LocalizationUtility.getResources(appContext)

    fun setDefaultLanguage(context: Context, language: String) {
        val locale = Locale(language)
        setDefaultLanguage(context, locale)
    }

    fun setDefaultLanguage(context: Context, language: String, country: String) {
        val locale = Locale(language, country)
        setDefaultLanguage(context, locale)
    }

    fun setDefaultLanguage(context: Context, locale: Locale) {
        LanguageSetting.setDefaultLanguage(context, locale)
    }
}
