package com.diadiem.pos_config.utils.localization.core

import android.content.Context


//https://github.com/akexorcist/Localization
fun Context.toLocalizedContext(): Context = LocalizationUtility.applyLocalizationConfig(this)
