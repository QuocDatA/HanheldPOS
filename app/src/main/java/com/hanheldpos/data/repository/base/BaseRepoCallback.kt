package com.hanheldpos.data.repository.base

/**
 * Created by: thongphm on 2020/11/05 - 17:08
 * Copyright (c) 2020
 */
interface BaseRepoCallback<T> {
    fun apiRequesting(showLoading: Boolean) = Unit
    fun apiResponse(data: T?)
    fun valueConflicted() = Unit
    fun showMessage(message: String?)
}