package com.hanheldpos.data.repository

import androidx.lifecycle.Lifecycle
import com.hanheldpos.data.repository.base.AutoDispose
import com.hanheldpos.data.repository.base.BaseRepo

abstract class BaseRxRepo(lifecycle: Lifecycle) : BaseRepo() {
    protected var autoDispose: AutoDispose = AutoDispose()

    init {
        autoDispose.bindTo(lifecycle)
    }
}