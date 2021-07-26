package com.hanheldpos.data.repository.base

/**
 * Created by: thongphm on 2020/11/05 - 17:08
 * Copyright (c) 2020
 */
interface BaseProgressCallback<T> : BaseRepoCallback<T> {
    fun showProgress(show: Boolean)
    fun progressUpdate(progress: Int)
}