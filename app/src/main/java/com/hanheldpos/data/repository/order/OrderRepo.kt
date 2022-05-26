package com.hanheldpos.data.repository.order

import com.hanheldpos.data.api.pojo.order.filter.OrderFilterResp
import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.order.status.OrderStatusResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query

class OrderRepo : BaseRepo() {
    fun getOrderSetting(
        userGuid: String?,
        locationGuid: String?,
        callback: BaseRepoCallback<BaseResponse<List<OrderSettingResp>>?>
    ) {
        callback.apiRequesting(true);
        orderService.getOrderSettings(userGuid = userGuid, location = locationGuid).enqueue(object :
            Callback<BaseResponse<List<OrderSettingResp>>?> {
            override fun onResponse(
                call: Call<BaseResponse<List<OrderSettingResp>>?>,
                response: Response<BaseResponse<List<OrderSettingResp>>?>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<BaseResponse<List<OrderSettingResp>>?>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        })
    }

    fun getOrderStatus(
        userGuid: String?,
        callback: BaseRepoCallback<BaseResponse<List<OrderStatusResp>>?>
    ) {
        callback.apiRequesting(true);
        orderService.getOrderStatus(userGuid = userGuid).enqueue(object :
            Callback<BaseResponse<List<OrderStatusResp>>?> {
            override fun onResponse(
                call: Call<BaseResponse<List<OrderStatusResp>>?>,
                response: Response<BaseResponse<List<OrderStatusResp>>?>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<BaseResponse<List<OrderStatusResp>>?>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        })

    }

    fun getOrderFilter(
        userGuid: String?,
        location: String?,
        deviceGuid: String?,
        diningOptionId: Int?,
        pageNo: Int?,
        pageSize: Int?,
        startDate: String?,
        endDate: String?,
        orderCode: String?,
        callback: BaseRepoCallback<BaseResponse<OrderFilterResp>?>
    ) {
        callback.apiRequesting(true);
        orderService.getOrderFilter(userGuid = userGuid,location,deviceGuid,diningOptionId,pageNo,pageSize,startDate,endDate,orderCode).enqueue(object :
            Callback<BaseResponse<OrderFilterResp>?> {
            override fun onResponse(
                call: Call<BaseResponse<OrderFilterResp>?>,
                response: Response<BaseResponse<OrderFilterResp>?>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<BaseResponse<OrderFilterResp>?>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        })

    }



}