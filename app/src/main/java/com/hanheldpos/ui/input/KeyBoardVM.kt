package com.hanheldpos.ui.input

import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hanheldpos.model.keyboard.KeyBoardType

class KeyBoardVM : ViewModel() {
    val input = MutableLiveData<String>()
    var listener: KeyBoardCallBack? = null;
    var keyBoardType = MutableLiveData<KeyBoardType>(KeyBoardType.Number);
    var isCapLock = MutableLiveData<Boolean>(false);
    fun concatenateInputString(view: View) {
        val textView = view as TextView
        if (input.value.isNullOrEmpty()) {
            input.value = ""
        }
        input.value = input.value?.plus(textView.text)
    }

    fun inputString(view: View) {
        val textView = view as TextView
        if (input.value.isNullOrEmpty()) {
            input.value = ""
            if(textView.text == "space") {
                input.value = (input.value + " ")
            } else {
                if(isCapLock.value!!){
                    input.value = (input.value + (textView.text).toString().uppercase())
                } else {
                    input.value = (input.value + textView.text)
                }
            }
        } else {
            if(textView.text == "space") {
                input.value = (input.value + " ")
            } else {
                if(isCapLock.value!!){
                    input.value = (input.value + (textView.text).toString().uppercase())
                } else {
                    input.value = (input.value + textView.text)
                }
            }
        }
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

    fun onCapLock() {
        isCapLock.postValue(!isCapLock.value!!)
    }

    fun switchKeyBoardType() {
        when(keyBoardType.value!!)  {
            KeyBoardType.Number -> {
                keyBoardType.postValue(KeyBoardType.Text)
            }
            KeyBoardType.Text -> {
                keyBoardType.postValue(KeyBoardType.Number)
            }
            KeyBoardType.TextOnly -> {}
            KeyBoardType.NumberOnly -> {}
        }

    }

    fun onListener(listener: KeyBoardCallBack) {
        this.listener = listener;
    }

    interface KeyBoardCallBack {
        fun onComplete();
        fun onCancel();
    }
}