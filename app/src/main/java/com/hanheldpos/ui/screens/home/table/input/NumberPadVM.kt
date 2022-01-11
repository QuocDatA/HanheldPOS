package com.hanheldpos.ui.screens.home.table.input

import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NumberPadVM : ViewModel()  {
    val input = MutableLiveData<String>()



    var listener : NumberPadCallBack? = null;

    fun concatenateInputString(view: View) {
        val textView = view as TextView
        if (input.value.isNullOrEmpty()) {
            input.value = ""
        }
        input.value = input.value?.plus(textView.text)
    }

    fun timesInput(view: View) {
        val textView = view as TextView
        if (input.value.isNullOrEmpty()) return

        val num = Character.getNumericValue(textView.text[1])
        input.value = (input.value?.toInt()?.times(num)).toString()
    }

    fun delete() {
        if (input.value.isNullOrEmpty()) return

        input.value = input.value?.substring(0, input.value?.length!! - 1)
    }

    fun onListener(listener : NumberPadCallBack) {
        this.listener = listener;
    }

    interface NumberPadCallBack {
        fun onComplete();
        fun onCancel();
    }
}