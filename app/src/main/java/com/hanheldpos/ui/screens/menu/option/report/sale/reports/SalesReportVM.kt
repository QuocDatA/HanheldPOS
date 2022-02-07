package com.hanheldpos.ui.screens.menu.option.report.sale.reports

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.setting.SettingDeviceResp
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.order.OrderAsyncRepo
import com.hanheldpos.data.repository.setting.SettingRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.order.OrderReq
import com.hanheldpos.model.order.OrderSubmitResp
import com.hanheldpos.model.report.SaleReportCustomData
import com.hanheldpos.model.setting.SettingDevicePut
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.menu.option.report.sale.reports.adapter.NumberDayReportItem
import com.hanheldpos.utils.GSonUtils
import com.hanheldpos.utils.time.DateTimeHelper

class SalesReportVM : BaseUiViewModel<SalesReportUV>() {

    var saleReportCustomData = MutableLiveData<SaleReportCustomData>(
        SaleReportCustomData(
            startDay = DateTimeHelper.curDate,
            endDay = DateTimeHelper.curDate,
            isCurrentDrawer = true,
            isAllDevice = false,
            isAllDay = false,
            startTime = "",
            endTime = ""
        )
    )

    private val settingRepo = SettingRepo();
    private val orderAlterRepo = OrderAsyncRepo();

    val numberOrder = MutableLiveData<Int>(DataHelper.ordersCompleted?.size ?: 0);

    fun onSyncOrders(view: View) {
        if (DataHelper.ordersCompleted?.size ?: 0 <= 0) return

        //TODO : sync order
        showLoading(true);
        val json = GSonUtils.toServerJson(
            SettingDevicePut(
                MaxChar = DataHelper.numberIncreaseOrder.toString().length.toLong(),
                NumberIncrement = DataHelper.numberIncreaseOrder.toString(),
                UserGuid = UserHelper.getUserGui(),
                LocationGuid = UserHelper.getLocationGui(),
                DeviceGuid = UserHelper.getDeviceGui(),
                Device_key = DataHelper.getDeviceByDeviceCode()?._key!!.toString()
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

        val listOrders = DataHelper.ordersCompleted;
        var countOrderPush = 0;
        val listPushSucceeded = mutableListOf<OrderReq>();
        listOrders?.forEach { orderReq ->
            // TODO : check if cart is pay or not
            val orderJson = GSonUtils.toServerJson(orderReq);
            orderAlterRepo.postOrderSubmit(orderJson, callback = object :
                BaseRepoCallback<OrderSubmitResp> {
                override fun apiResponse(data: OrderSubmitResp?) {


                    if (data == null || data.Message?.contains("exist") == true) {

                    } else {
                        listPushSucceeded.add(orderReq);
                    }

                    countOrderPush += 1;
                    if (countOrderPush >= listOrders.size) {
                        showLoading(false);
                        val list = listOrders.toMutableList().filter { it !in listPushSucceeded };
                        numberOrder.postValue(list.size);
                        DataHelper.ordersCompleted = list;
                    }
                }

                override fun showMessage(message: String?) {
                    countOrderPush += 1;
                    if (countOrderPush >= listOrders.size) {
                        showLoading(false);
                        val list = listOrders.toMutableList().filter { it !in listPushSucceeded };
                        numberOrder.postValue(list.size);
                        DataHelper.ordersCompleted = list;
                    }
                    AppAlertDialog.get()
                        .show(
                            context.getString(R.string.notification),
                            message,
                        )
                }
            })
        }
    }

    fun onPrint() {

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