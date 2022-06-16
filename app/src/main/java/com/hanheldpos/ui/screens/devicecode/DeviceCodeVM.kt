package com.hanheldpos.ui.screens.devicecode

import android.content.Context
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.device.Device
import com.hanheldpos.data.api.pojo.device.DeviceCodeResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.device.DeviceRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.SyncDataService
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.StringUtils
import java.util.*

class DeviceCodeVM : BaseUiViewModel<DeviceCodeUV>() {

    private var isDownloading = false

    val pinGroupSize = 4
    val pinTextLD = MutableLiveData<String>()
    private var mLastTimeClick: Long = 0

    // Fetch all data for storage local
    private val syncDataService by lazy { SyncDataService() }

    // Repository for Device code login
    private val repo: DeviceRepo by lazy { DeviceRepo() }


    fun signIn(view: View, isRecentAccount: Boolean) {
        // Limit click button
        if ((SystemClock.elapsedRealtime() - mLastTimeClick) < 1000) return
        mLastTimeClick = SystemClock.elapsedRealtime()

        // Fetch data
        uiCallback?.showLoading(true)
        val result: String = if (isRecentAccount) {
            DataHelper.recentDeviceCodeLocalStorage?.first()!!.AppCode
        } else {
            getPinWithSymbol(pinTextLD.value.toString())
        }
        Log.d("Uuid",StringUtils.getAndroidDeviceId(context = view.context));
        repo.getDataByAppCode(result,StringUtils.getAndroidDeviceId(context = view.context) ,object : BaseRepoCallback<BaseResponse<List<DeviceCodeResp>>> {
            override fun apiResponse(data: BaseResponse<List<DeviceCodeResp>>?) {
                if (data == null || data.DidError) {
                    showError(view.context?.getString(R.string.failed_to_load_data))
                } else {
                    DataHelper.deviceCodeLocalStorage = data.Model?.first()
                    addRecentDeviceKey(data.Model?.first()!!.Device)
                    loadResource(view.context)
                }
            }

            override fun showMessage(message: String?) {
                showLoading(false)
                showError(view.context?.getString(R.string.failed_to_load_data))
                uiCallback?.showMessage(view.context?.getString(R.string.failed_to_load_data))
            }
        })

    }

    private fun loadResource(context: Context) {
        if (isDownloading) return
        isDownloading = true
        syncDataService.fetchAllData(
            context,
            listener = object : SyncDataService.SyncDataServiceListener {
                override fun onLoadedResources() {
                    isDownloading = false
                    showLoading(false)
                    uiCallback?.openPinCode()
                }

                override fun onError(message: String?) {
                    isDownloading = false
                    showLoading(false)
                    showError(context.getString(R.string.failed_to_load_data))
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

    private fun addRecentDeviceKey(device: List<Device>) {
        if (DataHelper.recentDeviceCodeLocalStorage != null) {
            DataHelper.recentDeviceCodeLocalStorage!!.toMutableList()
                .indexOfFirst { it.AppCode == device.first().AppCode }
                .let {
                    val subList: MutableList<Device> =
                        DataHelper.recentDeviceCodeLocalStorage!!.toMutableList()
                    if (it == -1) {
                        subList.add(0, device.first())
                    } else {
                        subList.removeAt(it)
                        subList.add(0, device.first())
                    }
                    DataHelper.recentDeviceCodeLocalStorage =
                        subList.toList()
                }
        } else {
            DataHelper.recentDeviceCodeLocalStorage = device
        }
    }

    fun backPress() {
        uiCallback?.goBack()
    }
}