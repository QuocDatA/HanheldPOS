package com.hanheldpos.data.repository.report

import com.hanheldpos.data.api.pojo.floor.FloorResp
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query

class ReportRepo : BaseRepo() {
    fun getSalesReport(
        userGuid: String?,
        locationGuid: String?,
        deviceGuid: String?,
        employeeGuid: String?,
        day: String?,
        startHour: String?,
        cashDrawerGuid: String?,
        endHour: String?,
        isAllDevice: Boolean? = false,
        isCurrentCashdrawer: Boolean? = false,
        callback: BaseRepoCallback<BaseResponse<ReportSalesResp>?>
    ) {
        callback.apiRequesting(true);
        reportService.getSalesReport(
            userGuid = userGuid,
            locationGuid = locationGuid,
            deviceGuid,
            employeeGuid,
            day,
            startHour,
            cashDrawerGuid,
            endHour,
            isAllDevice,
            isCurrentCashdrawer,
        ).enqueue(object : Callback<BaseResponse<ReportSalesResp>?> {
            override fun onResponse(
                call: Call<BaseResponse<ReportSalesResp>?>,
                response: Response<BaseResponse<ReportSalesResp>?>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<BaseResponse<ReportSalesResp>?>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }
        })
    }
}
