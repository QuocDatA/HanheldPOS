package com.hanheldpos.data.repository.resource

import com.hanheldpos.data.api.pojo.resource.ResourceResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResourceRepo: BaseRepo() {
    fun getResource(
        userGuid: String?,
        locationGuid: String?,
        callback: BaseRepoCallback<BaseResponse<List<ResourceResp>>?>
    ) {
        callback.apiRequesting(true)
        resourceService.getResourceModel(userGuid = userGuid, location = locationGuid).enqueue(object :
            Callback<BaseResponse<List<ResourceResp>>?> {
            override fun onResponse(
                call: Call<BaseResponse<List<ResourceResp>>?>,
                response: Response<BaseResponse<List<ResourceResp>>?>
            ) {
                callback.apiRequesting(false)
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<BaseResponse<List<ResourceResp>>?>, t: Throwable) {
                callback.apiRequesting(false)
                t.printStackTrace()
                callback.showMessage(t.message)
            }

        })
    }
}