package com.hanheldpos.ui.screens.cashdrawer.startdrawer

import android.content.Context
import android.util.Log
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.cashdrawer.CreateCashDrawerResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.cashdrawer.CashDrawerRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cashdrawer.CashDrawerType
import com.hanheldpos.model.cashdrawer.CreateCashDrawerReq
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel
import com.hanheldpos.ui.screens.cashdrawer.CashDrawerHelper
import com.hanheldpos.utils.GSonUtils

class StartDrawerVM : BaseRepoViewModel<CashDrawerRepo, StartDrawerUV>() {

    var amount : Double = 0.0

    fun startDrawer(context: Context) {
        showLoading(true)
        val startingCash = amount
        val startDrawerReq = CreateCashDrawerReq(
            UserGuid = UserHelper.getUserGuid(),
            LocationGuid = UserHelper.getLocationGuid(),
            DeviceGuid = UserHelper.getDeviceGuid(),
            EmployeeGuid = UserHelper.getEmployeeGuid(),
            CashDrawerType = CashDrawerType.START.value,
            StartingCash = startingCash,
            ActualInDrawer = 0.0,
            DrawerDescription = ""
        )
        Log.d("Data Pass",GSonUtils.toServerJson(startDrawerReq))
        repo?.createCashDrawer(
            GSonUtils.toServerJson(startDrawerReq),object : BaseRepoCallback<BaseResponse<List<CreateCashDrawerResp>>?> {
                override fun apiResponse(data: BaseResponse<List<CreateCashDrawerResp>>?) {
                    showLoading(false)
                    if (data == null || data.DidError) {
                        CashDrawerHelper.isStartDrawer = false
                        showError(data?.ErrorMessage ?: context.getString(R.string.failed_to_load_data))
                    } else {
                        DataHelper.currentDrawerId = data.Model?.first()?.CashDrawerGuid
                        CashDrawerHelper.isStartDrawer = true
                        uiCallback?.goHome()
                    }
                }

                override fun showMessage(message: String?) {
                    showLoading(false)
                    showError(message?: "Some thing error.")
                }

            }
        )
    }

    override fun createRepo(): CashDrawerRepo {
        return CashDrawerRepo()
    }

}