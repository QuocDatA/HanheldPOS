package com.hanheldpos.data.repository.cashdrawer

import com.hanheldpos.data.api.pojo.cashdrawer.CashDrawerStatusResp
import com.hanheldpos.data.api.pojo.cashdrawer.CreateCashDrawerResp
import com.hanheldpos.data.api.pojo.cashdrawer.report.ReportCashDrawerResp
import com.hanheldpos.data.api.pojo.cashdrawer.pay_in_out.PaidInOutListResp
import com.hanheldpos.data.api.pojo.cashdrawer.pay_in_out.PayInOutResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CashDrawerRepo : BaseRepo() {
    fun getStatusCashDrawer(
        body: String,
        callback: BaseRepoCallback<BaseResponse<List<CashDrawerStatusResp>>?>
    ) {
        callback.apiRequesting(true);
        cashDrawerService.getStatusCashDrawer(body).enqueue(object :
            Callback<BaseResponse<List<CashDrawerStatusResp>>?> {
            override fun onResponse(
                call: Call<BaseResponse<List<CashDrawerStatusResp>>?>,
                response: Response<BaseResponse<List<CashDrawerStatusResp>>?>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(
                call: Call<BaseResponse<List<CashDrawerStatusResp>>?>,
                t: Throwable
            ) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        })
    }

    fun createCashDrawer(
        body: String,
        callback: BaseRepoCallback<BaseResponse<List<CreateCashDrawerResp>>?>
    ) {
        callback.apiRequesting(true);
        cashDrawerService.postCreateCashDrawer(body).enqueue(object :
            Callback<BaseResponse<List<CreateCashDrawerResp>>?> {
            override fun onResponse(
                call: Call<BaseResponse<List<CreateCashDrawerResp>>?>,
                response: Response<BaseResponse<List<CreateCashDrawerResp>>?>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(
                call: Call<BaseResponse<List<CreateCashDrawerResp>>?>,
                t: Throwable
            ) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        })
    }

    fun getReportCurrentDrawer(
        body: String,
        callback: BaseRepoCallback<BaseResponse<List<ReportCashDrawerResp>>?>
    ) {
        callback.apiRequesting(true);
        cashDrawerService.getReportCurrentDrawer(body)
            .enqueue(object : Callback<BaseResponse<List<ReportCashDrawerResp>>?> {
                override fun onResponse(
                    call: Call<BaseResponse<List<ReportCashDrawerResp>>?>,
                    response: Response<BaseResponse<List<ReportCashDrawerResp>>?>
                ) {
                    callback.apiRequesting(false);
                    callback.apiResponse(getBodyResponse(response));
                }

                override fun onFailure(
                    call: Call<BaseResponse<List<ReportCashDrawerResp>>?>,
                    t: Throwable
                ) {
                    callback.apiRequesting(false);
                    t.printStackTrace();
                    callback.showMessage(t.message);
                }
            })
    }

    fun getPaidInOutList(
        body: String,
        callback: BaseRepoCallback<BaseResponse<List<PaidInOutListResp>>?>
    ) {
        callback.apiRequesting(true);
        cashDrawerService.getListPaidInOut(body)
            .enqueue(object : Callback<BaseResponse<List<PaidInOutListResp>>?> {
                override fun onResponse(
                    call: Call<BaseResponse<List<PaidInOutListResp>>?>,
                    response: Response<BaseResponse<List<PaidInOutListResp>>?>
                ) {
                    callback.apiRequesting(false);
                    callback.apiResponse(getBodyResponse(response));
                }

                override fun onFailure(
                    call: Call<BaseResponse<List<PaidInOutListResp>>?>,
                    t: Throwable
                ) {
                    callback.apiRequesting(false);
                    t.printStackTrace();
                    callback.showMessage(t.message);
                }
            })
    }

    fun postPayInOut(
        body: String,
        callback: BaseRepoCallback<BaseResponse<List<PayInOutResp>>?>
    ) {
        callback.apiRequesting(true);
        cashDrawerService.postPaidInOut(body)
            .enqueue(object : Callback<BaseResponse<List<PayInOutResp>>?> {
                override fun onResponse(
                    call: Call<BaseResponse<List<PayInOutResp>>?>,
                    response: Response<BaseResponse<List<PayInOutResp>>?>
                ) {
                    callback.apiRequesting(false);
                    callback.apiResponse(getBodyResponse(response));
                }

                override fun onFailure(
                    call: Call<BaseResponse<List<PayInOutResp>>?>,
                    t: Throwable
                ) {
                    callback.apiRequesting(false);
                    t.printStackTrace();
                    callback.showMessage(t.message);
                }
            })
    }
}