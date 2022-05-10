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
import com.hanheldpos.model.report.SaleReportCustomData
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

    var saleReportCustomData = MutableLiveData(
        SaleReportCustomData(
            startDay = DateTimeUtils.curDate,
            endDay = DateTimeUtils.curDate,
            isCurrentDrawer = false,
            isAllDevice = false,
            isAllDay = true,
            startTime = null,
            endTime = null
        )
    )

    var isSyncOrderToServer: Boolean = false
    var saleReport: ReportSalesResp? = null

    private val settingRepo = SettingRepo();
    private val orderAlterRepo = OrderAsyncRepo();

    val numberOrder = Transformations.map(DatabaseHelper.ordersCompleted.getAll().asLiveData()) {
        return@map it.filter { order -> OrderHelper.isValidOrderPush(order) }.size
    }

    fun onSyncOrders(view: View,succeed : () -> Unit, failed: () -> Unit) {
        if (numberOrder.value ?: 0 <= 0){
            succeed()
            return
        }
        if (isSyncOrderToServer) {
            succeed()
            return
        }
        isSyncOrderToServer = true
        //TODO : sync order
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
        );
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
                        pushOrder(view.context);
                        succeed()
                    }
                }

                override fun showMessage(message: String?) {
                    failed()
                }
            });


    }

    private fun pushOrder(context: Context) {

        viewModelScope.launch(Dispatchers.IO) {
            val listOrdersFlow = DatabaseHelper.ordersCompleted.getAll()
            var countOrderPush = 0
            listOrdersFlow.take(1).collectLatest { listOrders ->
                listOrders.filter { OrderHelper.isValidOrderPush(it) }.let { listNeedPush ->
                    val listAfterSplit = listNeedPush.chunked(3)
                    listAfterSplit.forEach { subListNeedPush ->
                        val countPushNeed = countOrderPush + subListNeedPush.size
                        subListNeedPush.forEach { orderEntity ->
                            val orderReq = DatabaseMapper.mappingOrderReqFromEntity(orderEntity)
                            // TODO : check if cart is pay or not
                            val orderJson = GSonUtils.toServerJson(orderReq);
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
                                    if (countOrderPush >= listNeedPush.size) {
                                        isSyncOrderToServer = false
                                        showLoading(false)
                                    }
                                    viewModelScope.launch(Dispatchers.Main) {
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
                    }

                }

            }

        }

    }

    fun fetchDataSaleReport(succeed: () -> Unit,failed : ()-> Unit) {
        reportRepo.getSalesReport(
            userGuid = UserHelper.getUserGuid(),
            locationGuid = UserHelper.getLocationGuid(),
            deviceGuid = UserHelper.getDeviceGuid(),
            employeeGuid = UserHelper.getEmployeeGuid(),
            cashDrawerGuid = DataHelper.currentDrawerId,
            day = "${
                DateTimeUtils.dateToString(
                    saleReportCustomData.value?.startDay,
                    DateTimeUtils.Format.YYYY_MM_DD_18
                )
            }-${
                DateTimeUtils.dateToString(
                    saleReportCustomData.value?.endDay,
                    DateTimeUtils.Format.YYYY_MM_DD_18
                )
            }",
            startHour = saleReportCustomData.value?.startTime,
            endHour = saleReportCustomData.value?.endTime,
            isAllDevice = saleReportCustomData.value?.isAllDevice,
            isCurrentCashdrawer = saleReportCustomData.value?.isCurrentDrawer,
            callback = object : BaseRepoCallback<BaseResponse<ReportSalesResp>?> {
                override fun apiResponse(data: BaseResponse<ReportSalesResp>?) {

                    if (data == null || data.DidError) {
                        failed()
                    } else {
                        saleReport = data.Model
                        succeed()
                    }
                }

                override fun showMessage(message: String?) {
                    failed()
                }
            }
        )
    }
}