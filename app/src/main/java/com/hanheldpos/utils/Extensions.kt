package com.hanheldpos.utils

fun <T> concatenate(vararg lists: List<T>): List<T> {
    return listOf(*lists).flatten()
}