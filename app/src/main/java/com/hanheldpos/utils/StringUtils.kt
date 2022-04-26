package com.hanheldpos.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import java.security.AccessController.getContext
import java.text.Normalizer
import java.util.regex.Pattern

object StringUtils {
    fun removeAccent(s: String?): String? {
        s?:return null
        var result = s
        result = result.replace(Regex("[AÁÀÃẠÂẤẦẪẬĂẮẰẴẶ]"), "A");
        result = result.replace(Regex("[àáạảãâầấậẩẫăằắặẳẵ]"), "a");
        result = result.replace(Regex("[EÉÈẼẸÊẾỀỄỆ]"), "E");
        result = result.replace(Regex("[èéẹẻẽêềếệểễ]"), "e");
        result = result.replace(Regex("[IÍÌĨỊ]") , "I");
        result = result.replace(Regex("[ìíịỉĩ]"), "i");
        result = result.replace(Regex("[OÓÒÕỌÔỐỒỖỘƠỚỜỠỢ]"), "O");
        result = result.replace(Regex("[òóọỏõôồốộổỗơờớợởỡ]"), "o");
        result = result.replace(Regex("[UÚÙŨỤƯỨỪỮỰ]") , "U");
        result = result.replace(Regex("[ùúụủũưừứựửữ]"), "u");
        result = result.replace(Regex("[YÝỲỸỴ]"), "Y");
        result = result.replace(Regex("[ỳýỵỷỹ]"), "y");
        result = result.replace(Regex("Đ"), "D");
        result = result.replace(Regex("đ") , "d");
        // Some system encode vietnamese combining accent as individual utf-8 characters
        result = result.replace(Regex("\\u0300|\\u0301|\\u0303|\\u0309|\\u0323"), ""); // Huyền sắc hỏi ngã nặng
        result = result.replace(Regex("\\u02C6|\\u0306|\\u031B") , ""); // Â, Ê, Ă, Ơ, Ư
        return result;
    }

    @SuppressLint("HardwareIds")
    fun getAndroidDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID);
    }
}