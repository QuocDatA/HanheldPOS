package com.hanheldpos.binding

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.text.Editable
import android.text.Spanned
import android.text.TextUtils
import android.text.TextWatcher
import android.text.style.ReplacementSpan
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern


@BindingAdapter("useInputEnable")
fun setUseInputEnable(view: ViewPager2, isSwipe: Boolean) {
    view.isUserInputEnabled = isSwipe
    if (!isSwipe)
    {
        class NoPageTransformer : ViewPager2.PageTransformer {
            override fun transformPage( view: View, position: Float) {
                view.translationX = view.width * -position
                if (position <= -1.0f || position >= 1.0f) {
                    view.visibility = View.GONE
                } else if (position == 0.0f) {
                    view.visibility = View.VISIBLE
                } else {
                    view.visibility = View.GONE
                }
            }
        }
        view.setPageTransformer(NoPageTransformer());
    }
}

@BindingAdapter("visibleObject")
fun setVisibleObject(view: View, `object`: Any?) {
    if (visibleObject(`object`))
        view.visibility = View.VISIBLE
    else view.visibility = View.GONE
}


fun visibleObject(`object`: Any?): Boolean {
    var visibleObject = true
    if (`object` != null) {
        if (`object` is CharSequence) {
            if (TextUtils.isEmpty(`object` as CharSequence?) || `object` == "null") {
                visibleObject = false
            }
        } else if (`object` is Boolean) {
            if (!`object`) {
                visibleObject = false
            }
        } else if (`object` is Collection<*>) {
            if (`object`.size == 0) {
                visibleObject = false
            }
        }
    } else visibleObject = false
    return visibleObject
}

@BindingAdapter("groupSize")
fun setGroupSize(inputEditText: TextInputEditText?, groupSize: Int) {
    if (inputEditText == null) return
    inputEditText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(editable: Editable) {
            val special = Pattern.compile("[^A-Z0-9]", Pattern.CASE_INSENSITIVE)
            if (special.matcher(editable.toString()).find()) {
                val remain = editable.toString().replace("[^A-Z0-9]".toRegex(), "")
                inputEditText.setText(remain)
                inputEditText.setSelection(remain.length)
                return
            }
            val paddingSpans: Array<SpaceSpan> =
                editable.getSpans(0, editable.length, SpaceSpan::class.java)
            for (span in paddingSpans) {
                editable.removeSpan(span)
            }
            addSpans(editable)
        }

        private fun addSpans(editable: Editable) {
            val length = editable.length
            var i = 1
            while (i * groupSize < length) {
                val index = i * groupSize
                editable.setSpan(
                    SpaceSpan(), index - 1, index,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                i++
            }
        }

        inner class SpaceSpan : ReplacementSpan() {
            override fun getSize(
                paint: Paint,
                text: CharSequence,
                start: Int,
                end: Int,
                fm: FontMetricsInt?
            ): Int {
                val padding = paint.measureText(" ", 0, 1)
                val textSize = paint.measureText(text, start, end)
                return (padding + textSize).toInt()
            }

            override fun draw(
                canvas: Canvas,
                text: CharSequence,
                start: Int,
                end: Int,
                x: Float,
                top: Int,
                y: Int,
                bottom: Int,
                paint: Paint
            ) {
                canvas.drawText(
                    text.subSequence(start, end).toString() + " ",
                    x,
                    y.toFloat(),
                    paint
                )
            }
        }
    })
}

@BindingAdapter("backColor")
fun setBackColor(view : View?, colorHex : String? ){
    if(view == null || colorHex == null) return;
    if(!TextUtils.isEmpty(colorHex)){
        val color = Color.parseColor(colorHex)
        view.backgroundTintList = ColorStateList.valueOf(color);
        /*view.setBackgroundColor(color);*/
    }

}
