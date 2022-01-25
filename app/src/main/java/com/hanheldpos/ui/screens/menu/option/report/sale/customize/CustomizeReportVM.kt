package com.hanheldpos.ui.screens.menu.option.report.sale.customize

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class CustomizeReportVM : BaseUiViewModel<CustomizeReportUV>() {
    var isCurrentDrawer = MutableLiveData<Boolean>(true);
    var isAllDay = MutableLiveData<Boolean>(true);
    var isAllDevice = MutableLiveData<Boolean>(false)
    var isThisDevice = MutableLiveData<Boolean>(true)

    fun onDrawerCheckChange() {
        isCurrentDrawer.postValue(!isCurrentDrawer.value!!)
    }

    fun onAllDaySwitch() {
        isAllDay.postValue(!isAllDay.value!!)
    }

    fun onDeviceCheckChange() {
        isAllDevice.postValue(!isAllDevice.value!!)
        isThisDevice.postValue(!isThisDevice.value!!)
    }

    fun backPress() {
        uiCallback?.getBack()
    }
}