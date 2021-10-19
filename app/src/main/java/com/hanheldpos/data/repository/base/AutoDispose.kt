package com.hanheldpos.data.repository.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class AutoDispose internal constructor() : LifecycleObserver {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    fun bindTo(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    fun add(disposable: Disposable?) {
        compositeDisposable.add(disposable!!)
    }

    val isDestroy: Boolean
        get() = compositeDisposable.isDisposed

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        compositeDisposable.dispose()
    }

}