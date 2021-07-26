package com.utils.helper

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

/**
 * Created by: thongphm on 2021/02/23 - 14:25
 * Copyright (c) 2020
 */
object NumberHelper {

    private var defNumAfterDecimal = 0
    val groupingSeparator: Char = '.'
    val decimalSeparator: Char = ','

    fun setDefaultNumberAfterDecimal(numAfterDecimal: Int) {
        defNumAfterDecimal = numAfterDecimal
    }

    /**
     * Number with decimal format
     *
     *
     *
     * ●	Use comma (,) for thousand delimeter.
     * ●	Use dot (.) for decimal delimiter.
     * ●	Ex : 123,456.789
     * ●	Right align.
     *
     *
     * @param number
     * @return
     */
    @JvmStatic
    @JvmOverloads
    fun decimalToString(number: Double, numOfDecimal: Int? = 0): String {
        return String.format("%s", decimalFormal(numOfDecimal ?: 0).format(number))
    }

    @JvmStatic
    @JvmOverloads
    fun stringToDecimal(value: String?, numOfDecimal: Int? = 0): Double {
        if(value.isNullOrEmpty()) return 0.0
        return decimalFormal(numOfDecimal ?: 0).parse(value)?.toDouble() ?: 0.0
    }

    private fun decimalFormal(numOfDecimal: Int): NumberFormat {
        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
        val formatSymbols = DecimalFormatSymbols()

        // Use dot (.) for decimal delimiter.
        formatSymbols.decimalSeparator = decimalSeparator
        // Use comma (,) for thousand delimiter (if it's over 1000).
        formatSymbols.groupingSeparator = groupingSeparator
        // Apply symbol format
        (numberFormat as DecimalFormat).decimalFormatSymbols = formatSymbols
        numberFormat.setGroupingUsed(true)

        // Set fixed decimal.
        numberFormat.setMinimumFractionDigits(numOfDecimal)
        numberFormat.setMaximumFractionDigits(numOfDecimal)
        return numberFormat
    }
}