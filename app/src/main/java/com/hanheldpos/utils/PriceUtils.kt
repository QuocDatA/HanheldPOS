package com.hanheldpos.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object PriceUtils {
    fun formatStringPrice(price: String): String {
        if (price.isEmpty() || price.isBlank()) return "";
        val dfSymbols = DecimalFormatSymbols()
        dfSymbols.decimalSeparator = '.'
        dfSymbols.groupingSeparator = ','
        val df = DecimalFormat("###", dfSymbols)
        df.groupingSize = 3
        df.isGroupingUsed = true
        return df.format(price.replace(",", "").toDouble());
    }

    @JvmOverloads
    fun formatStringPrice(price: Double, limitLength: Boolean = false): String {

        var finalAmount = price

        if (limitLength && (price ?: 0.0) >= 1000000000) {
            finalAmount = (price ?: 0.0) / 1000
        }
        val dfSymbols = DecimalFormatSymbols()
        dfSymbols.decimalSeparator = '.'
        dfSymbols.groupingSeparator = ','
        val df = DecimalFormat("###", dfSymbols)
        df.groupingSize = 3
        df.isGroupingUsed = true
        var result = df.format(price.toString().replace(",", "").toDouble())
        finalAmount.let {
            result += (if (finalAmount != price) "K" else "")
        }
        return result;
    }
}