package com.hanheldpos.ui.screens.cashdrawer.enddrawer

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
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

class EndDrawerVM : BaseRepoViewModel<CashDrawerRepo,EndDrawerUV>() {

    override fun createRepo(): CashDrawerRepo {
        return CashDrawerRepo()
    }

    val amountExpected = MutableLiveData<Double>()
    val amountString = MutableLiveData<String>("0")
    val amount = MutableLiveData<Double>()
    val description = MutableLiveData<String>()

    fun initLifeCycle(owner : LifecycleOwner) {
        amountString.observe(owner, {
            if(it.isNullOrEmpty()){
                amount.postValue(0.0)
            }
            else
            amount.postValue(it.replace(",","").toDouble())
        })
    }

    fun endDrawer(context : Context) {
        showLoading(true)
        val endDrawerReq = CreateCashDrawerReq(
            UserGuid = UserHelper.getUserGuid(),
            LocationGuid = UserHelper.getLocationGuid(),
            DeviceGuid = UserHelper.getDeviceGuid(),
            EmployeeGuid = UserHelper.getEmployeeGuid(),
            CashDrawerType = CashDrawerType.END.value,
            StartingCash = 0.0,
            ActualInDrawer = amountExpected.value?.minus(amount.value?:0.0)?:0.0,
            DrawerDescription = description.value.toString(),
        )
        Log.d("Data Pass", GSonUtils.toServerJson(endDrawerReq))
        repo?.createCashDrawer(
            GSonUtils.toServerJson(endDrawerReq),object :
                BaseRepoCallback<BaseResponse<List<CreateCashDrawerResp>>?> {
                override fun apiResponse(data: BaseResponse<List<CreateCashDrawerResp>>?) {
                    showLoading(false)
                    if (data == null || data.DidError) {
                        CashDrawerHelper.isEndDrawer = false
                        showError(data?.ErrorMessage ?: context.getString(R.string.failed_to_load_data))
                    } else {
                        DataHelper.currentDrawerId = null
                        CashDrawerHelper.isEndDrawer = true
                        uiCallback?.onEndDrawer()
                    }
                }

                override fun showMessage(message: String?) {
                    showLoading(false)
                    showError(message?: "Some thing error.")
                }

            }
        )
    }

    fun backPress(){
        uiCallback?.backPress()
    }


}