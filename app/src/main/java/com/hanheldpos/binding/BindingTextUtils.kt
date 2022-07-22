package com.hanheldpos.binding

import android.annotation.SuppressLint
import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.hanheldpos.utils.PriceUtils

@BindingAdapter("priceView")
fun setPriceView(view: TextView?, price: Double?) {
    if (view == null || price == null) return
    view.text = PriceUtils.formatStringPrice(price)

}

@SuppressLint("SetTextI18n")
@BindingAdapter("pricePlusView")
fun setPricePlusView(view: TextView?, price: Double?) {
    if (view == null || price == null) return
    view.text = "+ ${PriceUtils.formatStringPrice(price)}"
}


@BindingAdapter("bindStrike")
fun setBindStrike(textView: TextView, isBind: Boolean?) {
    textView.apply {
        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}