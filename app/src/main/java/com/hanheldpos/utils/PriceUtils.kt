package com.hanheldpos.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object PriceUtils {
    fun formatStringPrice(price : String) : String {
        if (price.isEmpty() || price.isBlank()) return "";
        val dfSymbols = DecimalFormatSymbols()
        dfSymbols.decimalSeparator = '.'
        dfSymbols.groupingSeparator = ','
        val df = DecimalFormat("###", dfSymbols)
        df.groupingSize = 3
        df.isGroupingUsed = true
        return df.format(price.replace(",", "").toDouble());
    }

    fun formatStringPrice(price : Double) : String {
        val dfSymbols = DecimalFormatSymbols()
        dfSymbols.decimalSeparator = '.'
        dfSymbols.groupingSeparator = ','
        val df = DecimalFormat("###", dfSymbols)
        df.groupingSize = 3
        df.isGroupingUsed = true
        return df.format(price.toString().replace(",", "").toDouble());
    }
}