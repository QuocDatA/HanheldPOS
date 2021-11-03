package com.hanheldpos.data.repository.floor

import com.hanheldpos.data.api.pojo.table.TableResp
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FloorRepo : BaseRepo() {
    fun getPosFloor(
        userGuid: String?,
        locationGuid: String?,
        callback: BaseRepoCallback<TableResp?>
    ) {
        callback.apiRequesting(true);
        floorService.getPosFloor(
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