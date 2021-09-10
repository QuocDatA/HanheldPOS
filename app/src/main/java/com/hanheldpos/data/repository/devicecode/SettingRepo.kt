package com.hanheldpos.data.repository.devicecode

import android.annotation.SuppressLint
import androidx.lifecycle.Lifecycle
import com.hanheldpos.data.api.pojo.setting.devicecode.DeviceCodeResp
import com.hanheldpos.data.repository.BaseRxRepo
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingRepo() : BaseRepo() {

    fun getDataByAppCode(pinTextStr: String, callback: BaseRepoCallback<DeviceCodeResp>) {
        callback.apiRequesting(true)
        settingService.getDataByDeviceCode(pinTextStr).enqueue(object : Callback<DeviceCodeResp>{
            override fun onResponse(
                call: Call<DeviceCodeResp>,
                response: Response<DeviceCodeResp>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<DeviceCodeResp>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }
        })
    }

}