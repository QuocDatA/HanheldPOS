package com.hanheldpos.extension

import androidx.lifecycle.MutableLiveData


fun <T> MutableLiveData<T>.notifyValueChange() {
    value = value
}