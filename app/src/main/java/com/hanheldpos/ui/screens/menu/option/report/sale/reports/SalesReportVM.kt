package com.hanheldpos.ui.screens.menu.option.report.sale.reports

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.setting.SettingDeviceResp
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.order.OrderAsyncRepo
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
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.menu.option.report.sale.reports.adapter.NumberDayReportItem
import com.hanheldpos.utils.GSonUtils
import com.hanheldpos.utils.time.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class SalesReportVM : BaseUiViewModel<SalesReportUV>() {

    var isSyncOrderToServer: Boolean = false

    var saleReportCustomData = MutableLiveData<SaleReportCustomData>(
        SaleReportCustomData(
            startDay = DateTimeUtils.curDate,
            endDay = DateTimeUtils.curDate,
            isCurrentDrawer = true,
            isAllDevice = false,
            isAllDay = false,
            startTime = "",
            endTime = ""
        )
    )

    private val settingRepo = SettingRepo();
    private val orderAlterRepo = OrderAsyncRepo();

    val numberOrder = Transformations.map(DatabaseHelper.ordersCompleted.getAll().asLiveData()) {
        return@map it.filter { order -> OrderHelper.isValidOrderPush(order) }.size
    }

    fun onSyncOrders(view: View) {
        if (numberOrder.value ?: 0 <= 0) return
        if (isSyncOrderToServer) return
        isSyncOrderToServer = true
        //TODO : sync order
        showLoading(true);
        val json = GSonUtils.toServerJson(
            SettingDevicePut(
                MaxChar = DataHelper.numberIncreaseOrder.toString().length.toLong(),
                NumberIncrement = DataHelper.numberIncreaseOrder.toString(),
                UserGuid = UserHelper.getUserGuid(),
                LocationGuid = UserHelper.getLocationGuid(),
                DeviceGuid = UserHelper.getDeviceGuid(),
                Device_key = DataHelper.deviceCodeLocalStorage?.Device?.firstOrNull()?._key!!.toString()
            )
        );
        settingRepo.putSettingDeviceIds(
            json,
            callback = object : BaseRepoCallback<SettingDeviceResp> {
                override fun apiResponse(data: SettingDeviceResp?) {
                    if (data == null || data.DidError) {
                        showLoading(false);
                        AppAlertDialog.get()
                            .show(
                                view.context.getString(R.string.notification),
                                data?.ErrorMessage
                                    ?: view.context.getString(R.string.alert_msg_an_error_has_occurred),
                            )

                    } else {
                        pushOrder(view.context);
                    }
                }

                override fun showMessage(message: String?) {
                    showLoading(false);
                    showError(message);
                }
            });


    }

    private fun pushOrder(context: Context) {

        viewModelScope.launch(Dispatchers.IO) {
            val listOrdersFlow = DatabaseHelper.ordersCompleted.getAll()
            var countOrderPush = 0
            listOrdersFlow.take(1).collectLatest { listOrders ->
                listOrders.filter { OrderHelper.isValidOrderPush(it) }.let { listNeedPush ->
                    listNeedPush.forEach { orderEntity ->
                        val orderReq = DatabaseMapper.mappingOrderReqFromEntity(orderEntity)
                        // TODO : check if cart is pay or not
                        val orderJson = GSonUtils.toServerJson(orderReq);
                        orderAlterRepo.postOrderSubmit(orderJson, callback = object :
                            BaseRepoCallback<OrderSubmitResp> {
                            override fun apiResponse(data: OrderSubmitResp?) {
                                countOrderPush += 1
                                if (data == null || data.Message?.contains("exist") == true) {
                                    Log.d("Sync Order", "Post order failed!")
                                } else {
                                    viewModelScope.launch(Dispatchers.IO) {
                                        DatabaseHelper.ordersCompleted.update(
                                            orderEntity.apply { isSync = true })
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
                }

            }

        }

    }

    fun initNumberDaySelected(): MutableList<NumberDayReportItem> {
        return mutableListOf(
            NumberDayReportItem("1D", 1),
            NumberDayReportItem("2D", 2),
            NumberDayReportItem("3D", 3),
            NumberDayReportItem("5D", 5),
            NumberDayReportItem("1W", 7),
        )
    }

    fun onOpenCustomizeReport() {
        uiCallback?.onOpenCustomizeReport();
    }

    fun backPress() {
        uiCallback?.backPress();
    }
}