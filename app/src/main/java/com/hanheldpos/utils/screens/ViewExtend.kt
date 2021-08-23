package com.hanheldpos.utils.screens

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifyValueChange() {
    value = value
}

class ViewExtend {
}