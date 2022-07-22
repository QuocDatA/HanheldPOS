package com.hanheldpos.ui.screens.menu.report.current_drawer

import android.content.Context
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.cashdrawer.report.ReportCashDrawerResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.cashdrawer.CashDrawerRepo
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cashdrawer.CashDrawerStatusReq
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.GSonUtils

class CurrentDrawerVM : BaseUiViewModel<CurrentDrawerUV>() {

    private val cashDrawerRepo = CashDrawerRepo()

    fun onFragmentBackPressed() {
        uiCallback?.onFragmentBackPressed()
    }

    fun onOpenEndDrawer() {
        uiCallback?.onOpenEndDrawer()
    }

    fun onOpenPayInPayOut() {
        uiCallback?.onOpenPayInPayOut()
    }

    fun getCashDrawerDetail(context: Context) {
        showLoading(true)
        val bodyJson = GSonUtils.toServerJson(
            CashDrawerStatusReq(
                UserGuid = UserHelper.getUserGuid(),
                DeviceGuid = UserHelper.getDeviceGuid(),
                LocationGuid = UserHelper.getLocationGuid(),
                EmployeeGuid = UserHelper.getEmployeeGuid()
            )
        )
        cashDrawerRepo.getReportCurrentDrawer(
            bodyJson,
            object : BaseRepoCallback<BaseResponse<List<ReportCashDrawerResp>>?> {
                override fun apiResponse(data: BaseResponse<List<ReportCashDrawerResp>>?) {
                    showLoading(false)
                    if (data == null || data.DidError) {
                        AppAlertDialog.get()
                            .show(
                                context.resources.getString(R.string.notification),
                                message = data?.ErrorMessage
                                    ?: context.resources.getString(R.string.alert_msg_an_error_has_occurred),
                            )
                    } else
                        uiCallback?.showInfoCurrentDrawer(data.Model?.firstOrNull())
                }

                override fun showMessage(message: String?) {
                    showLoading(false)
                    AppAlertDialog.get()
                        .show(
                            context.resources.getString(R.string.notification),
                            message = message
                                ?: context.resources.getString(R.string.alert_msg_an_error_has_occurred),
                        )
                }
            },
        )
    }

}