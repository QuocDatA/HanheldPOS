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
import kotlinx.coroutines.*

class SettingsControlVM : BaseViewModel() {
    private var currentNotificationType: GeneralNotificationType? = null
    private var currentPushOrderType: GeneralPushType? = null
    val generalSetting = MutableLiveData<GeneralSetting>(DataHelper.generalSettingLocalStorage)
    val hardwareSetting = MutableLiveData<HardwareSetting>(DataHelper.hardwareSettingLocalStorage)
    var listener: SettingListener? = null
    fun init(owner: LifecycleOwner, listener: SettingListener) {
        this.listener = listener
        generalSetting.observe(owner) {
            if (it != null)
                DataHelper.generalSettingLocalStorage = it
            else return@observe
            if (DataHelper.isNeedToUpdateNewData.value == true) {
                if (it.notificationTime != currentNotificationType) {
                    currentNotificationType = it.notificationTime
                    startNotification(it.notificationTime?.value)
                }
            } else {
                disposeNotification()
            }
            if (it.automaticallyPushOrdersTime != GeneralPushType.MANUAL) {
                if (it.automaticallyPushOrdersTime != currentPushOrderType) {
                    currentPushOrderType = it.automaticallyPushOrdersTime
                    startAutomaticPushOrder(it.automaticallyPushOrdersTime?.value)
                }
            } else {
                disposeAutomaticPushOrder()
            }

        }
        hardwareSetting.observe(owner) {
            if (it != null)
                DataHelper.hardwareSettingLocalStorage = it
            else return@observe
        }
    }
    private val runNotification = fun(value: Int?): Job {
        return viewModelScope.launch {
            while ((value ?: -1) >= 0 && this.isActive) {
                launch(Dispatchers.Main ) {
                    listener?.onNotification()
                }
                delay(value?.toLong()?.times(1000) ?: 0)
            }
        }
    }
    private val runAutomaticPushOrder = fun(value: Int?): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            while ((value ?: -1) >= 0 && this.isActive) {
                launch(Dispatchers.Main) {
                    listener?.onPushOrder()
                }
                delay(value?.toLong()?.times(1000) ?: 0)
            }
        }
    }
    private var notificationRunningTaskFuture: Job? = null
    private var pushOrderRunningTaskFuture: Job? = null

    private fun startNotification(value: Int?) {
        if (notificationRunningTaskFuture != null) {
            disposeNotification()
        }
        val run = runNotification(value)
        notificationRunningTaskFuture = run
    }

    private fun disposeNotification() {
        notificationRunningTaskFuture?.cancel()
        notificationRunningTaskFuture = null
    }

    private fun startAutomaticPushOrder(value: Int?) {
        if (pushOrderRunningTaskFuture != null) {
            disposeAutomaticPushOrder()
        }
        val run = runAutomaticPushOrder(value)
        pushOrderRunningTaskFuture = run
    }

    private fun disposeAutomaticPushOrder() {
        pushOrderRunningTaskFuture?.cancel()
        pushOrderRunningTaskFuture = null
    }

    interface SettingListener {
        fun onNotification()
        fun onPushOrder()
    }
}