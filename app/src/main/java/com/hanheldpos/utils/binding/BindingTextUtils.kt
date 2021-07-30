package com.hanheldpos.utils.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.DecimalFormat

@BindingAdapter("priceView")
fun setPriceView(view: TextView?, price : Double?) {
    if(view == null) return;
    val formatter = DecimalFormat("###,###,###");
    val rs = formatter.format(price?.toInt()) + "đ";
    view.text = rs;
}