package com.hanheldpos.binding

import android.graphics.Paint
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.hanheldpos.utils.NumberTextWatcher
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

@BindingAdapter("priceView")
fun setPriceView(view: TextView?, price: Double?) {
    if (view == null || price == null) return

    view.addTextChangedListener(NumberTextWatcher(view as EditText))
}

@BindingAdapter("pricePlusView")
fun setPricePlusView(view: TextView?, price: Double?) {
    if (view == null || price == null) return
    view.addTextChangedListener(NumberTextWatcher(view as EditText))
}


@BindingAdapter("bindStrike")
fun setbindStrike(textView: TextView, isBind: Boolean?) {
    textView.apply {
        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}