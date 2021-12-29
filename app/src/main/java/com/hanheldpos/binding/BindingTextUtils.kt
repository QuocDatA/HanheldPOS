package com.hanheldpos.binding

import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

@BindingAdapter("priceView")
fun setPriceView(view: TextView?, price: Double?) {
    if (view == null || price == null) return
    val dfSymbols = DecimalFormatSymbols()
    dfSymbols.decimalSeparator = '.'
    dfSymbols.groupingSeparator = ','
    val df = DecimalFormat("###", dfSymbols)
    df.groupingSize = 3
    df.isGroupingUsed = true
    val rs = df.format(price);
    view.text = rs
}

@BindingAdapter("pricePlusView")
fun setPricePlusView(view: TextView?, price: Double?) {
    if (view == null || price == null) return
    val dfSymbols = DecimalFormatSymbols()
    dfSymbols.decimalSeparator = '.'
    dfSymbols.groupingSeparator = ','
    val df = DecimalFormat("###", dfSymbols)
    df.groupingSize = 3
    df.isGroupingUsed = true
    val rs = df.format(price);
    view.text = "+$rs"
}


@BindingAdapter("basePriceView")
fun setBasePriceView(view: TextView?, price: Double?) {
    if (view == null || price == null) return
    val dfSymbols = DecimalFormatSymbols()
    dfSymbols.decimalSeparator = '.'
    dfSymbols.groupingSeparator = ','
    val df = DecimalFormat("###", dfSymbols)
    df.groupingSize = 3
    df.isGroupingUsed = true
    val rs = df.format(price);
    view.text = "(Base price: $rs)"
}

@BindingAdapter("addPriceView")
fun setAddPriceView(view: TextView?, price: Double?) {
    if (view == null || price == null) return

    val dfSymbols = DecimalFormatSymbols()
    dfSymbols.decimalSeparator = '.'
    dfSymbols.groupingSeparator = ','
    val df = DecimalFormat("###", dfSymbols)
    df.groupingSize = 3
    df.isGroupingUsed = true
    val rs = df.format(price);
    view.text = "+$rs"
}

@BindingAdapter("priceDisc")
fun setDiscPriceView(view: TextView?, price: Double?) {
    if (view == null || price == null) return

    val dfSymbols = DecimalFormatSymbols()
    dfSymbols.decimalSeparator = '.'
    dfSymbols.groupingSeparator = ','
    val df = DecimalFormat("###", dfSymbols)
    df.groupingSize = 3
    df.isGroupingUsed = true
    val rs = df.format(price);

    view.text = "($rs)"
}

@BindingAdapter("priceDiscMinusRound")
fun setDiscPriceMinusRoundView(view: TextView?, price: Double?) {
    if (view == null || price == null) return

    val dfSymbols = DecimalFormatSymbols()
    dfSymbols.decimalSeparator = '.'
    dfSymbols.groupingSeparator = ','
    val df = DecimalFormat("###", dfSymbols)
    df.groupingSize = 3
    df.isGroupingUsed = true
    val rs = df.format(price);

    view.text = "(-$rs)"
}

@BindingAdapter("priceDiscMinus")
fun setDiscPriceMinusView(view: TextView?, price: Double?) {
    if (view == null || price == null) return

    val dfSymbols = DecimalFormatSymbols()
    dfSymbols.decimalSeparator = '.'
    dfSymbols.groupingSeparator = ','
    val df = DecimalFormat("###", dfSymbols)
    df.groupingSize = 3
    df.isGroupingUsed = true
    val rs = df.format(price);

    view.text = "-$rs"
}

@BindingAdapter("bindStrike")
fun setbindStrike(textView: TextView, isBind: Boolean?) {
    textView.apply {
        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}