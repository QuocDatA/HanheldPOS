package com.hanheldpos.ui.screens.menu.settings

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hanheldpos.data.api.pojo.setting.hardware.HardwareSetting
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.menu.settings.GeneralNotificationType
import com.hanheldpos.model.menu.settings.GeneralPushType
import com.hanheldpos.model.setting.GeneralSetting
import com.hanheldpos.ui.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class SettingsControlVM : BaseViewModel() {
    val generalSetting = MutableLiveData<GeneralSetting>(DataHelper.generalSettingLocalStorage)
    val hardwareSetting = MutableLiveData<HardwareSetting>(DataHelper.hardwareSettingLocalStorage)
    var listener: SettingListener? = null
    fun init(owner: LifecycleOwner, listener: SettingListener) {
        this.listener = listener
        generalSetting.observe(owner) {
            if (it != null)
                DataHelper.generalSettingLocalStorage = it
            if (DataHelper.isNeedToUpdateNewData.value == true) {
                startNotification(it.notificationTime?.value)
            } else {
                disposeNotification()
            }
            if (it.automaticallyPushOrdersTime != GeneralPushType.MANUAL) {
                startAutomaticPushOrder(it.automaticallyPushOrdersTime?.value)
            } else {
                disposeAutomaticPushOrder()
            }

        }
        hardwareSetting.observe(owner) {
            if (it != null)
                DataHelper.hardwareSettingLocalStorage = it
        }
    }

    private var notificationExecutorService: ExecutorService = Executors.newSingleThreadExecutor()
    private var pushOrderExecutorService: ExecutorService = Executors.newSingleThreadExecutor()
    private val runNotification = fun(value: Int?): Runnable {
        return Runnable {
            viewModelScope.launch {
                while ((value ?: -1) >= 0) {

                    launch(Dispatchers.Main) {
                        listener?.onNotification()
                    }
                    delay(value?.toLong()?.times(1000) ?: 0)
                }
            }
        }
    }
    private val runAutomaticPushOrder = fun(value: Int?): Runnable {
        return Runnable {
            viewModelScope.launch(Dispatchers.IO) {
                while ((value ?: -1) >= 0) {
                    launch(Dispatchers.Main) {
                        listener?.onPushOrder()
                    }
                    delay(value?.toLong()?.times(1000) ?: 0)
                }
            }
        }
    }
    private var notificationRunningTaskFuture: Future<*>? = null
    private var pushOrderRunningTaskFuture: Future<*>? = null


    private fun startNotification(value: Int?) {
        if (notificationRunningTaskFuture != null) {
            disposeNotification()
        }
        val run = runNotification(value)
        notificationRunningTaskFuture = notificationExecutorService.submit(run)
        run.run()
    }

    private fun disposeNotification() {
        notificationRunningTaskFuture?.cancel(true)
        notificationRunningTaskFuture = null
    }

    private fun startAutomaticPushOrder(value: Int?) {
        if (pushOrderRunningTaskFuture != null) {
            disposeAutomaticPushOrder()
        }
        val run = runAutomaticPushOrder(value)
        pushOrderRunningTaskFuture = pushOrderExecutorService.submit(run)
        run.run()
    }

    private fun disposeAutomaticPushOrder() {
        pushOrderRunningTaskFuture?.cancel(true)
        pushOrderRunningTaskFuture = null
    }

    interface SettingListener {
        fun onNotification()
        fun onPushOrder()
    }
}