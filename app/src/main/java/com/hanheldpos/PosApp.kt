package com.hanheldpos

import android.app.Application
import com.diadiem.pos_components.PAppCompatAutoCompleteTextView
import com.diadiem.pos_components.PMaterialButton
import com.diadiem.pos_components.PTextInputEditText
import com.diadiem.pos_components.PTextView
import com.diadiem.pos_config.AppConfig
import com.hanheldpos.model.DatabaseHelper
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.utils.helper.AppPreferences
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class PosApp : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this

        // Logger
        Logger.addLogAdapter(AndroidLogAdapter())
        // Preferences
        AppPreferences.get().init(this)
        // Custom Font
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/segoe_ui_*.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

        // config
        AppConfig.initConfig(this, "app_config.json")?.run {
            PTextView.initConfig(this)
            PMaterialButton.initConfig(this)
            PTextInputEditText.initConfig(this)
            PAppCompatAutoCompleteTextView.initConfig(this)
        }

        // database
        DatabaseHelper.initDatabase(applicationContext)


    }

    companion object {
        lateinit var instance: PosApp
            private set
    }
}