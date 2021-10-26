package com.hanheldpos.binding

import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.DecimalFormat

@BindingAdapter("priceView")
fun setPriceView(view: TextView?, price: Double?) {
    if (view == null || price == null) return
    val formatter = DecimalFormat("###,###,###")
    val rs = formatter.format(price.toInt())
    view.text = rs
}

@BindingAdapter("basePriceView")
fun setBasePriceView(view: TextView?, price: Double?) {
    if (view == null || price == null) return
    val formatter = DecimalFormat("###,###,###")
    val rs = formatter.format(price.toInt())
    view.text = "(Base price: $rs)"
}

@BindingAdapter("addPriceView")
fun setAddPriceView(view: TextView?, price: Double?) {
    if (view == null || price == null) return

    val formatter = DecimalFormat("###,###,###")
    val rs = formatter.format(price.toInt())

    view.text = "+$rs"
}

@BindingAdapter("priceDisc")
fun setDiscPriceView(view: TextView?, price: Double?) {
    if (view == null || price == null) return

    val formatter = DecimalFormat("###,###,###")
    val rs = formatter.format(price.toInt())

    view.text = "($rs)"
}

@BindingAdapter("bindStrike")
fun setbindStrike(textView: TextView, isBind: Boolean?) {
    textView.apply {
        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}