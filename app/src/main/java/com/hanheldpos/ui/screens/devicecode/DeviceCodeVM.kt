package com.hanheldpos.ui.screens.devicecode

import android.content.Context
import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.device.DeviceCodeResp
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.device.DeviceRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel
import java.util.*

class DeviceCodeVM : BaseRepoViewModel<DeviceRepo, DeviceCodeUV>() {
    val syncDataService = SyncDataService();
    val pinGroupSize = 4;
    val pinTextLD = MutableLiveData<String>();
    var context : Context? = null;

    private var mLastTimeClick: Long = 0;


    override fun createRepo(): DeviceRepo {
        return DeviceRepo();
    }

    fun initContext(context : Context) {
        this.context = context;

    }

    fun signIn() {
        if (SystemClock.elapsedRealtime() - mLastTimeClick < 1000) return;
        mLastTimeClick = SystemClock.elapsedRealtime();
        uiCallback?.showLoading(true);
        val result = getPinWithSymbol(pinTextLD.value.toString());
        repo?.getDataByAppCode(result, object : BaseRepoCallback<DeviceCodeResp> {
            override fun apiResponse(data: DeviceCodeResp?) {
                if (data == null || data.didError == true) {
                    showError(context?.getString(R.string.failed_to_load_data));
                } else {
                    DataHelper.deviceCodeResp = data;
                    loadResource();
                }
            }

            override fun showMessage(message: String?) {
                showError(message);
                uiCallback?.showMessage(message)
            }
        })
    }

    private fun loadResource(){
        showLoading(true);
        syncDataService.fetchAllData(context, listener = object : SyncDataService.SyncDataServiceListener{
            override fun onLoadedResources() {
                uiCallback?.openPinCode();
            }

            override fun onError(message: String?) {
                showLoading(false);
                showError(message);
            }
        });
    }

    private fun getPinWithSymbol(pinTextStr: String): String {
        val charList = LinkedList(pinTextStr.toList())
        if (charList.size > 5)
            charList.add(4, '-')
        if (charList.size > 10)
            charList.add(9, '-')

        return charList.joinToString(separator = "")
    }



    fun backPress() {
        uiCallback?.goBack();
    }
}