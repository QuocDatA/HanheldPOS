package com.hanheldpos.data.repository.data

import com.hanheldpos.data.api.pojo.data.DataVersion
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataRepo : BaseRepo() {
    fun getDataVersion(  userGuid: String?,
                         localeGuid : String?,deviceGuid : String?, callback: BaseRepoCallback<BaseResponse<DataVersion>>) {
        callback.apiRequesting(true)
        dataService.getDataVersion(userGuid,localeGuid,deviceGuid).enqueue(object :
            Callback<BaseResponse<DataVersion>> {
            override fun onResponse(
                call: Call<BaseResponse<DataVersion>>,
                response: Response<BaseResponse<DataVersion>>
            ) {
                callback.apiRequesting(false)
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<BaseResponse<DataVersion>>, t: Throwable) {
                callback.apiRequesting(false)
                t.printStackTrace()
                callback.showMessage(t.message)
            }
        })
    }
}