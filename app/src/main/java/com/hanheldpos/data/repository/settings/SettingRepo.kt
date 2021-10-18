package com.hanheldpos.data.repository.settings

import com.hanheldpos.data.api.pojo.order.menu.OrderMenuResp
import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.setting.DeviceCodeResp
import com.hanheldpos.data.api.pojo.table.TableResp
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingRepo() : BaseRepo() {

    fun getDataByAppCode(pinTextStr: String, callback: BaseRepoCallback<DeviceCodeResp>) {
        callback.apiRequesting(true)
        settingService.getDataByDeviceCode(pinTextStr).enqueue(object : Callback<DeviceCodeResp> {
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

    fun getOrderMenu(
        userGuid: String?,
        locationGuid: String?,
        callback: BaseRepoCallback<OrderMenuResp?>
    ) {
        callback.apiRequesting(true);
        settingService.getOrderMenu(
            userGuid = userGuid,
            location = locationGuid
        ).enqueue(object : Callback<OrderMenuResp> {
            override fun onResponse(call: Call<OrderMenuResp>, response: Response<OrderMenuResp>) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<OrderMenuResp>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }
        })
    }

    fun getOrderSetting(
        userGuid: String?,
        locationGuid: String?,
        callback: BaseRepoCallback<OrderSettingResp?>
    ) {
        callback.apiRequesting(true);
        settingService.getOrderSettings(userGuid = userGuid, location = locationGuid).enqueue(object : Callback<OrderSettingResp?>{
            override fun onResponse(
                call: Call<OrderSettingResp?>,
                response: Response<OrderSettingResp?>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<OrderSettingResp?>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        })
    }

    fun getPosFloor(
        userGuid: String?,
        locationGuid: String?,
        callback: BaseRepoCallback<TableResp?>
    ) {
        callback.apiRequesting(true);
        settingService.getPosFloor(
            userGuid = userGuid,
            location = locationGuid
        ).enqueue(object : Callback<TableResp> {
            override fun onResponse(call: Call<TableResp>, response: Response<TableResp>) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<TableResp>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }
        })
    }



}