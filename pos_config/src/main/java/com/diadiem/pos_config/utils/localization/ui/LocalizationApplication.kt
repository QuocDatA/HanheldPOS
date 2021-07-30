package com.diadiem.pos_config.utils.localization.ui

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.diadiem.pos_config.utils.localization.core.LocalizationApplicationDelegate
import java.util.*

//https://github.com/akexorcist/Localization
abstract class LocalizationApplication : Application() {
    private val localizationDelegate = LocalizationApplicationDelegate()

    override fun attachBaseContext(base: Context) {
        localizationDelegate.setDefaultLanguage(base, getDefaultLanguage())
        super.attachBaseContext(localizationDelegate.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localizationDelegate.onConfigurationChanged(this)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): Resources {
        return localizationDelegate.getResources(this)
    }

    abstract fun getDefaultLanguage(): Locale
}
