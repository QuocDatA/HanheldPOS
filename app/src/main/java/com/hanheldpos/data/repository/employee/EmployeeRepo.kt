package com.hanheldpos.data.repository.employee

import com.hanheldpos.data.api.pojo.employee.EmployeeResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepo
import com.hanheldpos.data.repository.base.BaseRepoCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmployeeRepo : BaseRepo() {
    fun getDataEmployee(
        userGuid: String,
        passCode: String,
        locationGuid: String, callback: BaseRepoCallback<BaseResponse<List<EmployeeResp>>>
    ) {
        callback.apiRequesting(true)
        employeeService.getDataEmployee(userGuid,passCode,locationGuid).enqueue(object : Callback<BaseResponse<List<EmployeeResp>>>{
            override fun onResponse(
                call: Call<BaseResponse<List<EmployeeResp>>>,
                response: Response<BaseResponse<List<EmployeeResp>>>
            ) {
                callback.apiRequesting(false)
                callback.apiResponse(getBodyResponse(response))
            }

            override fun onFailure(call: Call<BaseResponse<List<EmployeeResp>>>, t: Throwable) {
                callback.apiRequesting(false)
                t.printStackTrace()
                callback.showMessage(t.message)
            }
        })
    }
}