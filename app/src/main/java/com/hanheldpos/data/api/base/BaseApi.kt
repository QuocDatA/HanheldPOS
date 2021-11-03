package com.hanheldpos.data.api.base

import com.hanheldpos.data.api.RetrofitService
import com.hanheldpos.data.api.services.*

abstract class BaseApi : BaseApiError() {

    // Retrofit
    private val retrofit = RetrofitService.get();
    // Services
    protected val orderService : OrderService = retrofit.createService(OrderService::class.java);
    protected val employeeService : EmployeeService = retrofit.createService(EmployeeService::class.java);
    protected val deviceService : DeviceService = retrofit.createService(DeviceService::class.java);
    protected val feeService : FeeService = retrofit.createService(FeeService::class.java);
    protected val floorService: FloorService = retrofit.createService(FloorService::class.java);
    protected val menuService : MenuService = retrofit.createService(MenuService::class.java);

}