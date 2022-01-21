package com.hanheldpos.data.repository.floor

import com.hanheldpos.data.api.pojo.floor.FloorResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FloorRepo : BaseRepo() {
    fun getPosFloor(
        userGuid: String?,
        locationGuid: String?,
        callback: BaseRepoCallback<BaseResponse<List<FloorResp>>?>
    ) {
        callback.apiRequesting(true);
        floorService.getPosFloor(
            userGuid = userGuid,
            location = locationGuid
        ).enqueue(object : Callback<BaseResponse<List<FloorResp>>?> {
            override fun onResponse(call: Call<BaseResponse<List<FloorResp>>?>, response: Response<BaseResponse<List<FloorResp>>?>) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<BaseResponse<List<FloorResp>>?>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }
        })
    }
}