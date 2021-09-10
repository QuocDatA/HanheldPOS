package com.hanheldpos.ui.screens.devicecode

import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.setting.devicecode.DeviceCodeResp
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.devicecode.SettingRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel
import java.util.*

class DeviceCodeVM : BaseRepoViewModel<SettingRepo,DeviceCodeUV>() {

    val pinGroupSize = 4;
    val pinTextLD = MutableLiveData<String>();

    private var mLastTimeClick : Long = 0;

    fun signIn() {
        if (SystemClock.elapsedRealtime() - mLastTimeClick < 1000) return;
        mLastTimeClick = SystemClock.elapsedRealtime();
        uiCallback?.showLoading(true);
        val result = getPinWithSymbol(pinTextLD.value.toString());
        repo?.getDataByAppCode(result,object : BaseRepoCallback<DeviceCodeResp>{
            override fun apiResponse(data: DeviceCodeResp?) {
                onDataSuccess(data);
            }
            override fun showMessage(message: String?) {
                showError(message);
            }
        })
    }
    private fun getPinWithSymbol(pinTextStr: String): String {
        val charList = LinkedList(pinTextStr.toList())
        if (charList.size > 5)
            charList.add(4, '-')
        if (charList.size > 10)
            charList.add(9, '-')

        return charList.joinToString(separator = "")
    }

    private fun onDataSuccess(result: DeviceCodeResp?){
        uiCallback?.showLoading(false)

        result?.let {
            if (it.didError == true) {
                showError(it.message);
            } else {
                DataHelper.deviceCodeResp = result
                uiCallback?.openPinCode();
            }
        }
    }

    fun backPress() {
        uiCallback?.goBack();
    }

    override fun createRepo(): SettingRepo {
        return SettingRepo();
    }


}