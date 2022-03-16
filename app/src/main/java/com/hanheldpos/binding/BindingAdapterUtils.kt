package com.hanheldpos.binding

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.text.*
import android.text.style.ReplacementSpan
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.textfield.TextInputEditText


@BindingAdapter("useInputEnable")
fun setUseInputEnable(view: ViewPager2, isSwipe: Boolean) {
    view.isUserInputEnabled = isSwipe
    if (!isSwipe) {
        class NoPageTransformer : ViewPager2.PageTransformer {
            override fun transformPage(view: View, position: Float) {
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

@BindingAdapter("marquee")
fun setTextViewMarquee(textView: TextView, isMarquee: Boolean) {
    if (isMarquee) {
        textView.isSingleLine = true;
        textView.ellipsize = TextUtils.TruncateAt.MARQUEE;
        textView.isHorizontalFadingEdgeEnabled = true;
        textView.marqueeRepeatLimit = -1;
        textView.canScrollHorizontally(1);
        textView.isSelected = true;
    }
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
    inputEditText.filters = arrayOf(getCustomInputFilter(
        allowCharacters = true,
        allowDigits = true,
        allowSpaceChar = false
    ))


    inputEditText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(editable: Editable) {
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
                val padding = paint.measureText("  ", 0, 2)
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

private fun getCustomInputFilter(
    allowCharacters: Boolean,
    allowDigits: Boolean,
    allowSpaceChar: Boolean
): InputFilter {
    return object : InputFilter {
        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            var keepOriginal = true
            val sb = StringBuilder(end - start)
            for (i in start until end) {
                val c = source[i]
                if (isCharAllowed(c)) {
                    sb.append(c.toUpperCase())
                } else {
                    keepOriginal = false
                }
            }
            return if (keepOriginal) {
                null
            } else {
                if (source is Spanned) {
                    val sp = SpannableString(sb)
                    TextUtils.copySpansFrom(source, start, sb.length, null, sp, 0)
                    sp
                } else {
                    sb
                }
            }
        }

        private fun isCharAllowed(c: Char): Boolean {
            if (Character.isLetter(c) && allowCharacters) {
                return true
            }
            if (Character.isDigit(c) && allowDigits) {
                return true
            }
            return Character.isSpaceChar(c) && allowSpaceChar
        }
    }
}

@BindingAdapter("backColor")
fun setBackColor(view: View?, colorHex: String?) {
    if (view == null || colorHex == null) return;
    if (!TextUtils.isEmpty(colorHex)) {
        try {
            val color = Color.parseColor(colorHex)
            view.backgroundTintList = ColorStateList.valueOf(color);
        } catch (e: Exception) {
            val color = Color.parseColor("#8f8f8f");
            view.backgroundTintList = ColorStateList.valueOf(color);
        }
        /*view.setBackgroundColor(color);*/
    }

}
