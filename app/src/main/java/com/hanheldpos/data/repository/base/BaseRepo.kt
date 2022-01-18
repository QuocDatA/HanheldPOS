package com.hanheldpos.data.repository.base

import com.hanheldpos.data.api.base.BaseApi
import com.hanheldpos.data.repository.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by: thongphm on 2020/11/05 - 13:40
 * Copyright (c) 2020
 */
abstract class BaseRepo : BaseApi() {

    protected fun <T> getApiCallback(callback: BaseRepoCallback<T>): Callback<BaseResponse<T>> {
        return object : Callback<BaseResponse<T>> {
            override fun onResponse(
                call: Call<BaseResponse<T>>,
                response: Response<BaseResponse<T>>
            ) {
                callback.run {
                    apiRequesting(false)
                    if (response.isSuccessful) {
                        apiResponse(getBodyResponse(response)?.Model)
                        return
                    }
                    showMessage(getErrMessage(response))
                }
            }

            override fun onFailure(call: Call<BaseResponse<T>>, t: Throwable) {
                callback.run {
                    apiRequesting(false)
                    showMessage(t.message)
                }
            }
        }
    }

    protected fun <T> getBodyResponse(response: Response<T>): T? {

        if (response.isSuccessful) {
            val bodyResponse = response.body()
            bodyResponse?.let { body ->
                return body
            }
        }
        else {
            val bodyResponse = response.body()
            bodyResponse?.let { body ->
                return body
            }
        }
        return null
    }
}