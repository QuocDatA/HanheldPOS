package com.hanheldpos.ui.screens.input

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hanheldpos.model.keyboard.KeyBoardType

class KeyBoardVM(type: KeyBoardType, private val maxLength: Int? = null) : ViewModel() {

    private val input = MutableLiveData<String>()
    var listener: KeyBoardCallBack? = null;
    var keyBoardType = MutableLiveData<KeyBoardType>(KeyBoardType.Number);
    var isCapLock = MutableLiveData<Boolean>(false);

    init {
        keyBoardType.postValue(type)
    }

    fun concatenateInputString(view: View) {
        maxLength?.let {
            if (input.value?.length ?: 0 >= it) return
        }
        val textView = view as TextView
        if (input.value.isNullOrEmpty()) {
            input.value = ""
        }
        input.value = input.value?.plus(textView.text)
    }

    fun inputString(view: View) {
        maxLength?.let {
            if (input.value?.length ?: 0 >= it) return
        }

        val textView = view as TextView
        if (input.value.isNullOrEmpty()) {
            input.value = ""
            if (textView.text == "space") {
                input.value = (input.value + " ")
            } else {
                if (isCapLock.value!!) {
                    input.value = (input.value + (textView.text).toString().uppercase())
                } else {
                    input.value = (input.value + textView.text)
                }
            }
        } else {
            if (textView.text == "space") {
                input.value = (input.value + " ")
            } else {
                if (isCapLock.value!!) {
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

    fun onCapLock(capLock: Boolean) {
        isCapLock.postValue(capLock)
    }

    fun clearText() {
        input.postValue("")
    }

    fun switchKeyBoardType() {
        when (keyBoardType.value!!) {
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

    fun onListener(
        owner: LifecycleOwner,
        view: EditText,
        listener: KeyBoardCallBack,
        initInput: String = ""
    ) {
        input.observe(owner) {
            view.setText(it)
        }
        input.postValue(initInput)
        this.listener = listener;
    }


    interface KeyBoardCallBack {
        fun onComplete();
        fun onCancel();
    }
}