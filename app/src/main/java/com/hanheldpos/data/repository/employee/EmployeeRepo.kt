package com.hanheldpos.data.repository.employee

import retrofit2.Callback
import com.hanheldpos.data.api.pojo.employee.EmployeeResp
import com.hanheldpos.data.repository.GDataResp
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Response

class EmployeeRepo() : BaseRepo() {
    fun getDataEmployee(
        userGuid: String,
        passCode: String,
        locationGuid: String, callback: BaseRepoCallback<GDataResp<EmployeeResp>>
    ) {
        callback.apiRequesting(true);
        employeeService.getDataEmployee(userGuid,passCode,locationGuid).enqueue(object : Callback<GDataResp<EmployeeResp>>{
            override fun onResponse(
                call: Call<GDataResp<EmployeeResp>>,
                response: Response<GDataResp<EmployeeResp>>
            ) {
                callback.apiRequesting(false);
                callback.apiResponse(getBodyResponse(response));
            }

            override fun onFailure(call: Call<GDataResp<EmployeeResp>>, t: Throwable) {
                callback.apiRequesting(false);
                t.printStackTrace();
                callback.showMessage(t.message);
            }

        });
    }
}