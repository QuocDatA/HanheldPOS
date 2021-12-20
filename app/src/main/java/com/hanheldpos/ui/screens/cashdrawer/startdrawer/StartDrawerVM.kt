package com.hanheldpos.ui.screens.cashdrawer.startdrawer

import android.bluetooth.BluetoothClass
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.R
import com.hanheldpos.data.PosConst
import com.hanheldpos.data.api.pojo.cashdrawer.CreateCashDrawerResp
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.cashdrawer.CashDrawerRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cashdrawer.CashDrawerType
import com.hanheldpos.model.cashdrawer.CreateCashDrawerReq
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.JsonHelper

class StartDrawerVM : BaseRepoViewModel<CashDrawerRepo,StartDrawerUV>() {

    var context : Context? = null;
    val amountString = MutableLiveData<String>(0.toString());
    val amount = Transformations.map(amountString) {
        return@map it.replace(",","").toDouble();
    }
    fun backPress(){
        uiCallback?.backPress();
    }

    fun initContext(context: Context) {
        this.context = context;
    }


    fun onComplete() {
        showLoading(true);
        val startDrawerReq = CreateCashDrawerReq(
            UserGuid = UserHelper.getUserGui(),
            LocationGuid = UserHelper.getLocationGui(),
            DeviceGuid = UserHelper.getDeviceGui(),
            EmployeeGuid = UserHelper.getEmployeeGuid(),
            CashDrawerType = CashDrawerType.START.value,
            StartingCash = amount.value,
            ActualInDrawer = 0.0,
            DrawerDescription = ""
        );
        Log.d("Data Pass",JsonHelper.stringify(startDrawerReq));
        repo?.createCashDrawer(
            JsonHelper.stringify(startDrawerReq),object : BaseRepoCallback<CreateCashDrawerResp> {
                override fun apiResponse(data: CreateCashDrawerResp?) {
                    showLoading(false)
                    if (data == null || data.DidError) {
                        showError(context?.getString(R.string.failed_to_load_data));
                    } else {
                        DataHelper.CurrentDrawer_id = data.Model.first().CashDrawerGuid;
                        uiCallback?.goHome();
                    }
                }

                override fun showMessage(message: String?) {
                    showLoading(false)
                    showError(message);
                }

            }
        )
    }

    override fun createRepo(): CashDrawerRepo {
        return CashDrawerRepo();
    }

}