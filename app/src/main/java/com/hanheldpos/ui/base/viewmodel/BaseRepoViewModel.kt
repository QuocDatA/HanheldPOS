package com.hanheldpos.ui.base.viewmodel

import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.ui.base.BaseUserView

abstract class BaseRepoViewModel<T : BaseRepo, V : BaseUserView> : BaseViewModel() {

    protected abstract fun createRepo(): T

    var repo: T? = null
        get() {
            if (field == null) {
                field = createRepo()
            }
            return field
        }

    protected var uiCallback: V? = null

    fun init(uiCallback: V) {
        this.uiCallback = uiCallback
    }
}