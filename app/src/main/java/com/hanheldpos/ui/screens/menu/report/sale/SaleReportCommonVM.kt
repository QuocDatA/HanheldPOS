package com.hanheldpos.ui.screens.menu.report.sale

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.report.ReportSalesResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.order.OrderAsyncRepo
import com.hanheldpos.data.repository.report.ReportRepo
import com.hanheldpos.data.repository.setting.SettingRepo
import com.hanheldpos.database.DatabaseMapper
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.DatabaseHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.order.OrderSubmitResp
import com.hanheldpos.model.report.ReportFilterModel
import com.hanheldpos.model.setting.SettingDevicePut
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.viewmodel.BaseViewModel
import com.hanheldpos.utils.DateTimeUtils
import com.hanheldpos.utils.GSonUtils
import com.hanheldpos.utils.StringUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class SaleReportCommonVM : BaseViewModel() {
    private val reportRepo = ReportRepo()

    private val defaultReportFilter = ReportFilterModel(
        startDay = DateTimeUtils.dateToString(
            DateTimeUtils.curDate,
            DateTimeUtils.Format.YYYY_MM_DD
        ),
        endDay = DateTimeUtils.dateToString(DateTimeUtils.curDate, DateTimeUtils.Format.YYYY_MM_DD),
        isCurrentCashDrawer = true,
        isAllDevice = false,
        startHour = null,
        endHour = null
    )
    var reportFilter = MutableLiveData(
        defaultReportFilter
    )

    fun resetDefaultReport() = reportFilter.postValue(defaultReportFilter)

    var isSyncOrderToServer: Boolean = false
    val reportRequestHistory = MutableLiveData<List<ReportFilterModel>>(mutableListOf())

    private val settingRepo = SettingRepo()
    private val orderAlterRepo = OrderAsyncRepo()

    val orderCompleted = Transformations.map(DatabaseHelper.ordersCompleted.getAllLiveData().asLiveData()) {
        return@map it.filter { order -> OrderHelper.isValidOrderPush(order) }
    }

    val numberOrder = Transformations.map(orderCompleted) {
        return@map it.size
    }

    val numberNewRequestHistory = Transformations.map(reportRequestHistory) {
        return@map it.filter { report -> !(report.isRead ?: true) }.size
    }


    fun onSyncOrders(view: View, succeed: () -> Unit, failed: () -> Unit) {
        if ((numberOrder.value ?: 0) <= 0) {
            succeed()
            return
        }
        if (isSyncOrderToServer) {
            return
        }
        isSyncOrderToServer = true

        val json = GSonUtils.toServerJson(
            SettingDevicePut(
                MaxChar = DataHelper.numberIncreaseOrder.toString().length.toLong(),
                NumberIncrement = DataHelper.numberIncreaseOrder.toString(),
                UserGuid = UserHelper.getUserGuid(),
                LocationGuid = UserHelper.getLocationGuid(),
                DeviceGuid = UserHelper.getDeviceGuid(),
                Device_key = DataHelper.deviceCodeLocalStorage?.Device?.firstOrNull()?._key!!.toString(),
                uuid = StringUtils.getAndroidDeviceId(context = view.context)
            )
        )
        settingRepo.putSettingDeviceIds(
            json,
            callback = object : BaseRepoCallback<BaseResponse<String>> {
                override fun apiResponse(data: BaseResponse<String>?) {
                    if (data == null || data.DidError) {
                        failed()
                        AppAlertDialog.get()
                            .show(
                                view.context.getString(R.string.notification),
                                data?.ErrorMessage
                                    ?: view.context.getString(R.string.alert_msg_an_error_has_occurred),
                            )
                    } else {
                        pushOrder(view.context, succeed, failed)
                    }
                }

                override fun showMessage(message: String?) {
                    failed()
                }
            })


    }

    private fun pushOrder(context: Context, succeed: () -> Unit, failed: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val listOrdersFlow = DatabaseHelper.ordersCompleted.getAllLiveData()
            var countOrderPush = 0
            listOrdersFlow.take(1).collectLatest { listOrders ->
                listOrders.filter { OrderHelper.isValidOrderPush(it) }.let { listNeedPush ->
                    val listAfterSplit = listNeedPush.chunked(3)
                    listAfterSplit.forEach { subListNeedPush ->
                        val countPushNeed = countOrderPush + subListNeedPush.size
                        subListNeedPush.forEach { orderEntity ->
                            val orderReq = DatabaseMapper.mappingOrderReqFromEntity(orderEntity)
                            // TODO : check if cart is pay or not
                            val orderJson = GSonUtils.toServerJson(orderReq)
                            orderAlterRepo.postOrderSubmit(orderJson, callback = object :
                                BaseRepoCallback<BaseResponse<OrderSubmitResp>> {
                                override fun apiResponse(data: BaseResponse<OrderSubmitResp>?) {
                                    countOrderPush += 1
                                    if (data == null || data.Message?.contains("exist") == true || data.DidError) {
                                        Log.d("Sync Order", "Post order failed!")
                                        viewModelScope.launch(Dispatchers.IO) {
                                            DatabaseHelper.ordersCompleted.update(
                                                orderEntity.apply {
                                                    requestLogJson = GSonUtils.toJson(data)
                                                }
                                            )
                                        }
                                    } else {
                                        viewModelScope.launch(Dispatchers.IO) {
                                            DatabaseHelper.ordersCompleted.update(
                                                orderEntity.apply {
                                                    isSync = true; requestLogJson =
                                                    GSonUtils.toJson(data)
                                                })
                                        }
                                    }
                                    if (countOrderPush >= listNeedPush.size) {
                                        viewModelScope.launch(Dispatchers.IO) {
                                            launch(Dispatchers.Main) {
                                                isSyncOrderToServer = false
                                                showLoading(false)
                                            }
                                        }

                                    }
                                }

                                override fun showMessage(message: String?) {
                                    countOrderPush += 1
                                    viewModelScope.launch(Dispatchers.Main) {
                                        if (countOrderPush >= listNeedPush.size) {
                                            isSyncOrderToServer = false
                                            showLoading(false)
                                        }

                                        AppAlertDialog.get()
                                            .show(
                                                context.getString(R.string.notification),
                                                message,
                                            )
                                    }
                                }
                            })
                        }
                        while (countOrderPush < countPushNeed) {

                        }
                        viewModelScope.launch(Dispatchers.Main) {
                            succeed()
                        }

                    }

                }

            }

        }

    }

    fun fetchDataSaleReport(
        filter: ReportFilterModel?,
        succeed: (report: ReportSalesResp) -> Unit,
        failed: (message: String?) -> Unit
    ) {
        reportRepo.getSalesReport(
            userGuid = UserHelper.getUserGuid(),
            locationGuid = UserHelper.getLocationGuid(),
            deviceGuid = UserHelper.getDeviceGuid(),
            employeeGuid = UserHelper.getEmployeeGuid(),
            cashDrawerGuid = DataHelper.currentDrawerId,
            day = "${
                DateTimeUtils.strToStr(
                    filter?.startDay,
                    DateTimeUtils.Format.YYYY_MM_DD,
                    DateTimeUtils.Format.YYYY_MM_DD_18
                )
            }-${
                DateTimeUtils.strToStr(
                    filter?.endDay,
                    DateTimeUtils.Format.YYYY_MM_DD,
                    DateTimeUtils.Format.YYYY_MM_DD_18
                )
            }",
            startHour = filter?.startHour,
            endHour = filter?.endHour,
            isAllDevice = filter?.isAllDevice,
            isCurrentCashDrawer = filter?.isCurrentCashDrawer,
            callback = object : BaseRepoCallback<BaseResponse<ReportSalesResp>?> {
                override fun apiResponse(data: BaseResponse<ReportSalesResp>?) {
                    if (data == null || data.DidError || data.Model == null) {
                        failed.invoke(data?.Message)
                    } else {
                        succeed.invoke(data.Model)
                    }
                }

                override fun showMessage(message: String?) {
                    failed.invoke(message)
                }
            }
        )
    }
}