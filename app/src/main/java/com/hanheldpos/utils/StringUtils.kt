package com.hanheldpos.utils

import java.lang.StringBuilder
import java.text.Normalizer
import java.util.regex.Pattern

object StringUtils {
    fun removeAccent(s: String?): String? {
        val temp: String = Normalizer.normalize(s, Normalizer.Form.NFD)
        val pattern: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(temp).replaceAll("")
    }

}