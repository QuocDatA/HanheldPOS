package com.hanheldpos.data.repository.receipt

import com.hanheldpos.data.api.pojo.receipt.ReceiptCashier
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReceiptRepo : BaseRepo() {

    fun getReceiptCashiers(
        userGuid: String?,
        localeGuid : String?,
        callback: BaseRepoCallback<BaseResponse<List<ReceiptCashier>>>
    ) {
        callback.apiRequesting(true)
        receiptService.getReceiptCashier(userGuid = userGuid,localeGuid).enqueue(object :
            Callback<BaseResponse<List<ReceiptCashier>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<ReceiptCashier>>>,
                response: Response<BaseResponse<List<ReceiptCashier>>>
            ) {
                callback.apiRequesting(false)
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(
                call: Call<BaseResponse<List<ReceiptCashier>>>,
                t: Throwable
            ) {
                callback.apiRequesting(false)
                t.printStackTrace()
                callback.showMessage(t.message)
            }

        })
    }
}