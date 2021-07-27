package com.hanheldpos.ui.screens.devicecode

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import kotlinx.coroutines.*

class DeviceCodeVM : BaseUiViewModel<DeviceCodeUV>() {

    val pinGroupSize = 4;
    val pinTextLD = MutableLiveData<String>();

    fun signIn() {
        uiCallback?.showLoading(true);

        // Test Result
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000L);
            uiCallback?.showLoading(false);
            uiCallback?.openPinCode();
        }
    }

    fun backPress() {
        uiCallback?.goBack();
    }



}