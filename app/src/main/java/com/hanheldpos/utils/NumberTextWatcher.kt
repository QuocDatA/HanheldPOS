package com.hanheldpos.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.diadiem.pos_config.utils.Const
import java.text.DecimalFormat
import java.text.ParseException




class NumberTextWatcher(et: EditText ) : TextWatcher {

    private val df: DecimalFormat = DecimalFormat("#,###.##")
    private val dfnd: DecimalFormat
    private val prefix: String = Const.SymBol.VND
    private var hasFractionalPart: Boolean
    private val et: EditText
    override fun afterTextChanged(s: Editable) {
        et.removeTextChangedListener(this)
        try {
            val inilen: Int = et.text.length
            val v = s.toString().replace(df.decimalFormatSymbols.groupingSeparator.toString(), "")
                .replace(prefix, "")
            val n = df.parse(v)
            val cp = et.selectionStart
            var text: String? = prefix
            text += if (hasFractionalPart) {
                df.format(n)
            } else {
                dfnd.format(n)
            }
            et.setText(text)
            val endlen: Int = et.text.length
            val sel = cp + (endlen - inilen)
            if (sel > 0 && sel <= et.text.length) {
                et.setSelection(sel)
            } else {
                // place cursor at the end?
                et.setSelection(et.text.length - 1)
            }
        } catch (nfe: NumberFormatException) {
            // do nothing?
        } catch (e: ParseException) {
            // do nothing?
        }
        et.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        hasFractionalPart =
            s.toString().contains(df.decimalFormatSymbols.decimalSeparator.toString())
    }

    companion object {
        private const val TAG = "NumberTextWatcher"
    }

    init {
        df.isDecimalSeparatorAlwaysShown = true
        dfnd = DecimalFormat("#,###")
        this.et = et
        hasFractionalPart = false
    }
}