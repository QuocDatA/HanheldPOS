package com.hanheldpos.extension

import com.diadiem.pos_config.utils.Const
import kotlin.math.floor

fun Boolean?.isError(): Boolean {
    if (this == true) {
        return true
    }
    return false
}

inline fun Boolean?.ifTrue(block: Boolean.() -> Unit): Boolean? {
    if (this == true) {
        block()
    }
    return this
}

inline fun Boolean?.ifFalse(block: Boolean?.() -> Unit): Boolean? {
    if (null == this || !this) {
        block()
    }

    return this
}

inline fun String?.isHTTP(block: String?.() -> Unit): Boolean? {
    if (null != this && contains(Const.SymBol.HTTP)) {
        block()
    }
    return null
}

fun Double.isLong(): Boolean {
    if (floor(this) == this) {
        return true
    }
    return false
}

/**
 * If double is 20 then to string --> 20.0
 * If use this method then --> 20
 */
fun Double.toNiceString(): String {
    return if (this.isLong()) {
        String.format("%d", this.toLong())
    } else {
        this.toString()
    }
}

inline fun <T> List<T>?.hasValue(block: (List<T>) -> Unit): Boolean? {
    if (null != this && this.isNotEmpty()) {
        block(this)
    }
    return null
}

fun <T> mergeList(first: List<T>, second: List<T>): List<T> {
    val list: MutableList<T> = mutableListOf()
    list.addAll(first)
    list.addAll(second)
    return list
}
