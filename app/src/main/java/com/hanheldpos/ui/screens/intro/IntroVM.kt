package com.hanheldpos.ui.screens.intro

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import kotlinx.coroutines.*

class IntroVM : BaseUiViewModel<IntroUV>() {

    private var introJob: Job? = null
    private val introDelay: Long = 500

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    /**
     * Start intro with delay
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startIntro() {
        introJob = CoroutineScope(Dispatchers.IO).launch {
            delay(introDelay)
            withContext(Dispatchers.Main) {
                uiCallback?.finishIntro()
            }
        }
        introJob!!.start()
    }

    /**
     * Stop intro
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopIntro() {
        introJob?.cancel()
        introJob = null
    }

}