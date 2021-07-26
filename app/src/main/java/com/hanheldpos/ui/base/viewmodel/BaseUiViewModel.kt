package com.hanheldpos.ui.base.viewmodel

import com.hanheldpos.ui.base.BaseUserView

abstract class BaseUiViewModel<V : BaseUserView> : BaseViewModel() {

    protected var uiCallback: V? = null

    fun init(uiCallback: V) {
        this.uiCallback = uiCallback
    }
}