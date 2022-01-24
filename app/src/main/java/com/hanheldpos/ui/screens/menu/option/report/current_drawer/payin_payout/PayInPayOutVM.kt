package com.hanheldpos.ui.screens.menu.option.report.current_drawer.payin_payout


import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.cashdrawer.pay_in_out.PaidInOutListResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.cashdrawer.CashDrawerRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.payinout.PaidInOutListCashDrawerReq
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.GSonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PayInPayOutVM : BaseUiViewModel<PayInPayOutUV>() {

    private val repo = CashDrawerRepo();

    val amountString = MutableLiveData<String>();
    var amount: Double? = null;
    var description = MutableLiveData<String>();

    val isValid = MediatorLiveData<Boolean>().apply {
        value = false;
    };

    enum class ActiveButton {
        PayIn,
        PayOut,
    }

    val isActiveButton = MutableLiveData<ActiveButton?>();

    init {
        isValid.addSource(amountString) {
            amount = if (it.isNullOrEmpty()) {
                null;
            } else
                it.replace(",", "").toDouble();
            isValid.value = if (amount == null || description.value == null) {
                false
            } else {
                amount!! > 0.0 && !description.value?.trim().isNullOrEmpty();
            }
        }
        isValid.addSource(description) {
            isValid.value = if (amount == null || description.value == null) {
                false
            } else {
                amount!! > 0.0 && !description.value?.trim().isNullOrEmpty();
            }
        }
    }

    fun loadPaidInOut(context: Context) {
        showLoading(true);
        val bodyJson = GSonUtils.toServerJson(
            PaidInOutListCashDrawerReq(
                UserGuid = DataHelper.getUserGuidByDeviceCode(),
                LocationGuid = DataHelper.getLocationGuidByDeviceCode(),
                DeviceGuid = DataHelper.getDeviceGuidByDeviceCode(),
                EmployeeGuid = UserHelper.getEmployeeGuid(),
                CashDrawerGuid = DataHelper.currentDrawerId,
            )
        );
        repo.getPaidInOutList(
            bodyJson,
            callback = object : BaseRepoCallback<BaseResponse<List<PaidInOutListResp>>?> {
                override fun apiResponse(data: BaseResponse<List<PaidInOutListResp>>?) {
                    showLoading(false);
                    if (data == null || data.DidError) {
                        AppAlertDialog.get()
                            .show(
                                context.resources.getString(R.string.notification),
                                message = data?.ErrorMessage
                                    ?: context.resources.getString(R.string.alert_msg_an_error_has_occurred),
                            );
                    } else {
                        uiCallback?.onLoadPaidInOutListToUI(data.Model);
                    }
                }

                override fun showMessage(message: String?) {
                    showLoading(false);
                    AppAlertDialog.get()
                        .show(
                            context.resources.getString(R.string.notification),
                            message = message
                                ?: context.resources.getString(R.string.alert_msg_an_error_has_occurred),
                        );
                }
            },
        )


    }

    fun onPayInClick() {
        when (isActiveButton.value) {
            ActiveButton.PayIn -> {

            }
            ActiveButton.PayOut -> {
                isActiveButton.postValue(null);
            }
            null -> {
                isActiveButton.postValue(ActiveButton.PayIn);
            }
        }
    }

    fun onPayoutClick() {
        when (isActiveButton.value) {
            ActiveButton.PayIn -> {
                isActiveButton.postValue(null);
            }
            ActiveButton.PayOut -> {

            }
            null -> {
                isActiveButton.postValue(ActiveButton.PayOut);
            }
        }
    }

    fun backPress() {
        uiCallback?.getBack()
    }


}