package com.hanheldpos.binding

import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.DecimalFormat

@BindingAdapter("priceView")
fun setPriceView(view: TextView?, price : Double?) {
    if(view == null || price == null) return;
    val formatter = DecimalFormat("###,###,###");
    val rs = formatter.format(price?.toInt()) + "đ";
    view.text = rs;
}

@BindingAdapter("bindStrike")
fun bindStrike(textView: TextView, isBind: Boolean?) {
    textView.apply {
        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}