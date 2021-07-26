package com.hanheldpos

import android.app.Application
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
    }

    companion object {
        lateinit var instance: PosApp
            private set
    }
}