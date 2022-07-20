package com.hanheldpos.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData


fun <T> MutableLiveData<T>.notifyValueChange() {
    value = value
}

class CombinedLiveData<R>(vararg liveDatas: LiveData<*>,
                          private val combine: (datas: List<Any?>) -> R) : MediatorLiveData<R>() {

    private val datas: MutableList<Any?> = MutableList(liveDatas.size) { null }

    init {
        for(i in liveDatas.indices){
            super.addSource(liveDatas[i]) {
                datas[i] = it
                value = combine(datas)
            }
        }
    }
}