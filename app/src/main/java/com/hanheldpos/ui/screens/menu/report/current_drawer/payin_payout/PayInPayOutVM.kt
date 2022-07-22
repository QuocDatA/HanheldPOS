package com.hanheldpos.ui.screens.menu.report.current_drawer.payin_payout


import android.content.Context
import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.cashdrawer.pay_in_out.PaidInOutListResp
import com.hanheldpos.data.api.pojo.cashdrawer.pay_in_out.PayInOutResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.cashdrawer.CashDrawerRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.payinout.PaidInOutListCashDrawerReq
import com.hanheldpos.model.payinout.PayInOutCashDrawerReq
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.GSonUtils

class PayInPayOutVM : BaseUiViewModel<PayInPayOutUV>() {

    private val repo = CashDrawerRepo()

    val amountString = MutableLiveData<String>()
    var amount: Double? = null
    var description = MutableLiveData<String>()

    val isValid = MediatorLiveData<Boolean>().apply {
        value = false
    }

    enum class ActiveButton {
        PayIn,
        PayOut,
    }

    val isActiveButton = MutableLiveData<ActiveButton?>()

    init {
        isValid.addSource(amountString) {
            amount = if (it.isNullOrEmpty()) {
                null
            } else
                it.replace(",", "").toDouble()
            isValid.value = if (amount == null || description.value == null) {
                false
            } else {
                amount!! > 0.0 && !description.value?.trim().isNullOrEmpty()
            }
        }
        isValid.addSource(description) {
            isValid.value = if (amount == null || description.value == null) {
                false
            } else {
                amount!! > 0.0 && !description.value?.trim().isNullOrEmpty()
            }
        }
    }

    fun loadPaidInOut(context: Context) {
        showLoading(true)
        val bodyJson = GSonUtils.toServerJson(
            PaidInOutListCashDrawerReq(
                UserGuid = OrderHelper.getUserGuidByDeviceCode(),
                LocationGuid = OrderHelper.getLocationGuidByDeviceCode(),
                DeviceGuid = OrderHelper.getDeviceGuidByDeviceCode(),
                EmployeeGuid = UserHelper.getEmployeeGuid(),
                CashDrawerGuid = DataHelper.currentDrawerId,
            )
        )
        repo.getPaidInOutList(
            bodyJson,
            callback = object : BaseRepoCallback<BaseResponse<List<PaidInOutListResp>>?> {
                override fun apiResponse(data: BaseResponse<List<PaidInOutListResp>>?) {
                    showLoading(false)
                    if (data == null || data.DidError) {
                        AppAlertDialog.get()
                            .show(
                                context.resources.getString(R.string.notification),
                                message = data?.ErrorMessage
                                    ?: context.resources.getString(R.string.alert_msg_an_error_has_occurred),
                            )
                    } else {
                        uiCallback?.onLoadPaidInOutListToUI(data.Model)
                    }
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

    fun onPayInClick(view : View) {
        if (isValid.value == true)
        when (isActiveButton.value) {
            ActiveButton.PayIn -> {
                postPayInOut(ActiveButton.PayIn,view.context)
            }
            ActiveButton.PayOut -> {
                isActiveButton.postValue(null)
            }
            null -> {
                isActiveButton.postValue(ActiveButton.PayIn)
            }
        }
    }

    fun onPayoutClick(view : View) {
        if (isValid.value == true)
        when (isActiveButton.value) {
            ActiveButton.PayIn -> {
                isActiveButton.postValue(null)
            }
            ActiveButton.PayOut -> {
                postPayInOut(ActiveButton.PayOut,view.context)
            }
            null -> {
                isActiveButton.postValue(ActiveButton.PayOut)
            }
        }
    }

    private fun postPayInOut(button : ActiveButton,context: Context){
        showLoading(true)
        val bodyJson = GSonUtils.toServerJson(PayInOutCashDrawerReq(
            UserGuid = OrderHelper.getUserGuidByDeviceCode(),
            LocationGuid = OrderHelper.getLocationGuidByDeviceCode(),
            DeviceGuid = OrderHelper.getDeviceGuidByDeviceCode(),
            EmployeeGuid = UserHelper.getEmployeeGuid(),
            CashDrawerGuid = DataHelper.currentDrawerId,
            Source = null,
            Description = description.value!!,
            Payable = if (button == ActiveButton.PayOut) amount?:0.0 else 0.0,
            Receivable = if (button == ActiveButton.PayIn) amount?:0.0 else 0.0,
        ))
        repo.postPayInOut(bodyJson, callback = object : BaseRepoCallback<BaseResponse<List<PayInOutResp>>?>{
            override fun apiResponse(data: BaseResponse<List<PayInOutResp>>?) {
                if (data == null || data.DidError) {
                    AppAlertDialog.get()
                        .show(
                            context.resources.getString(R.string.notification),
                            message = data?.ErrorMessage
                                ?: context.resources.getString(R.string.alert_msg_an_error_has_occurred),
                        )
                } else {
                    description.postValue("")
                    amountString.postValue("")
                    loadPaidInOut(context)
                }
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
        })

    }

    fun backPress() {
        uiCallback?.onFragmentBackPressed()
    }


}