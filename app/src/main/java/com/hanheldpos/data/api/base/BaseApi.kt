package com.hanheldpos.data.api.base

import com.hanheldpos.data.api.RetrofitService
import com.hanheldpos.data.api.RetrofitServiceAsync
import com.hanheldpos.data.api.services.*

abstract class BaseApi : BaseApiError() {

    // Retrofit
    private val retrofit = RetrofitService.get();
    private val retrofitAsync = RetrofitServiceAsync.get();
    // Services
    protected val orderService : OrderService = retrofit.createService(OrderService::class.java);
    protected val employeeService : EmployeeService = retrofit.createService(EmployeeService::class.java);
    protected val deviceService : DeviceService = retrofit.createService(DeviceService::class.java);
    protected val feeService : FeeService = retrofit.createService(FeeService::class.java);
    protected val floorService: FloorService = retrofit.createService(FloorService::class.java);
    protected val menuService : MenuService = retrofit.createService(MenuService::class.java);
    protected val discountService : DiscountService = retrofit.createService(DiscountService::class.java);
    protected val customerService : CustomerService = retrofit.createService(CustomerService::class.java);
    protected val paymentService : PaymentService = retrofit.createService(PaymentService::class.java);
    protected val settingService : SettingService = retrofit.createService(SettingService::class.java);
    protected val cashDrawerService : CashDrawerService = retrofit.createService(CashDrawerService::class.java);
    protected val systemService : SystemService = retrofit.createService(SystemService::class.java);

    protected val orderAsyncService : OrdersAsyncService = retrofitAsync.createService(OrdersAsyncService::class.java);
}