package com.hanheldpos.ui.screens.devicecode

import android.content.Context
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.device.DeviceCodeResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.device.DeviceRepo
import com.hanheldpos.database.DatabaseMapper
import com.hanheldpos.database.PosDatabase
import com.hanheldpos.database.repo.DeviceCodeLocalRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.DatabaseHelper
import com.hanheldpos.model.SyncDataService
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.coroutineContext

class DeviceCodeVM : BaseUiViewModel<DeviceCodeUV>() {
    val pinGroupSize = 4;
    val pinTextLD = MutableLiveData<String>();
    private var mLastTimeClick: Long = 0;

    // Fetch all data for storage local
    private val syncDataService by lazy { SyncDataService() }

    // Repository for Device code login
    private val repo: DeviceRepo by lazy { DeviceRepo() }


    fun signIn(view: View) {
        // Limit click button
        if (SystemClock.elapsedRealtime() - mLastTimeClick < 1000) return;
        mLastTimeClick = SystemClock.elapsedRealtime();

        // Fetch data
        uiCallback?.showLoading(true);
        val result = getPinWithSymbol(pinTextLD.value.toString());
        repo.getDataByAppCode(result, object : BaseRepoCallback<BaseResponse<DeviceCodeResp>> {
            override fun apiResponse(data: BaseResponse<DeviceCodeResp>?) {
                if (data == null || data.DidError) {
                    showError(view.context?.getString(R.string.failed_to_load_data));
                } else {
                    viewModelScope.launch(Dispatchers.IO) {
                        if (data.Model != null)
                            DatabaseHelper.deviceCodeLocalRepo?.insert(
                                DatabaseMapper.mappingDeviceCodeToEntity(
                                    data.Model
                                )
                            )
                        DatabaseHelper.deviceCodeLocalRepo?.getAll()?.let {
                            Log.d("Test Storage", it.toString())
                        }
                        launch (Dispatchers.Default){
                            DataHelper.deviceCode = data.Model;
                            loadResource(view.context);
                        }
                    }

                }
            }

            override fun showMessage(message: String?) {
                showLoading(false);
                showError(message);
                uiCallback?.showMessage(message)
            }
        })
    }

    private fun loadResource(context: Context) {
        syncDataService.fetchAllData(
            context,
            listener = object : SyncDataService.SyncDataServiceListener {
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