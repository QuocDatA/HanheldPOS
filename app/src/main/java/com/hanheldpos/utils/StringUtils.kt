package com.hanheldpos.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import java.security.AccessController.getContext
import java.text.Normalizer
import java.util.regex.Pattern

object StringUtils {
    fun removeAccent(s: String?): String? {
        val temp: String = Normalizer.normalize(s, Normalizer.Form.NFD)
        val pattern: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(temp).replaceAll("")
    }

    @SuppressLint("HardwareIds")
    fun getAndroidDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID);
    }
}