package com.hanheldpos.ui.base.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel(), LifecycleObserver {

    val viewState = SingleLiveEvent<Int>()
    var errMessage: String? = null

    /**
     * Show / Hide loading
     */
    fun showLoading(show: Boolean) {
        viewState.value = if (show) ViewState.SHOW_LOADING else ViewState.HIDE_LOADING
    }

    /**
     * Show error
     */
    fun showError(errMessage: String?) {
        this.errMessage = errMessage
        viewState.value = ViewState.SHOW_ERROR
    }
}