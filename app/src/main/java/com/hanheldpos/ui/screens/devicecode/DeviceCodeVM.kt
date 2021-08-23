package com.hanheldpos.ui.screens.devicecode

import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import kotlinx.coroutines.*

class DeviceCodeVM : BaseUiViewModel<DeviceCodeUV>() {

    val pinGroupSize = 4;
    val pinTextLD = MutableLiveData<String>();
    private var mLastTimeClick : Long = 0;

    fun signIn() {
        if (SystemClock.elapsedRealtime() - mLastTimeClick < 1000) return;
        mLastTimeClick = SystemClock.elapsedRealtime();
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