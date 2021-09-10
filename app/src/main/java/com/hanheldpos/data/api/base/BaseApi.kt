package com.hanheldpos.data.api.base

import com.hanheldpos.data.api.RetrofitService
import com.hanheldpos.data.api.services.devicecode.SettingService
import com.hanheldpos.data.api.services.employee.EmployeeService

abstract class BaseApi : BaseApiError() {

    // Retrofit
    private val retrofit = RetrofitService.get();
    // Services
    protected val settingService : SettingService = retrofit.createService(SettingService::class.java);
    protected val employeeService : EmployeeService = retrofit.createService(EmployeeService::class.java);
}