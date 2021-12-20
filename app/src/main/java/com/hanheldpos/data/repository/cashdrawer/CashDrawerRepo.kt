package com.hanheldpos.data.repository.cashdrawer

import com.hanheldpos.data.api.pojo.cashdrawer.CashDrawerStatusResp
import com.hanheldpos.data.api.pojo.cashdrawer.CreateCashDrawerResp
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.model.cashdrawer.CreateCashDrawerReq
import com.hanheldpos.model.order.OrderSubmitResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CashDrawerRepo : BaseRepo() {
    fun getStatusCashDrawer(
        body : String,
        callback: BaseRepoCallback<CashDrawerStatusResp>
    ) {
        callback.apiRequesting(true);
        cashDrawerService.getStatusCashDrawer(body).enqueue(object :
            Callback<CashDrawerStatusResp> {
            override fun onResponse(
                call: Call<CashDrawerStatusResp>,
                response: Response<CashDrawerStatusResp>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<CashDrawerStatusResp>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        })
    }

    fun createCashDrawer(body : String, callback: BaseRepoCallback<CreateCashDrawerResp>) {
        callback.apiRequesting(true);
        cashDrawerService.postCreateCashDrawer(body).enqueue(object :
            Callback<CreateCashDrawerResp> {
            override fun onResponse(
                call: Call<CreateCashDrawerResp>,
                response: Response<CreateCashDrawerResp>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<CreateCashDrawerResp>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        })
    }
}