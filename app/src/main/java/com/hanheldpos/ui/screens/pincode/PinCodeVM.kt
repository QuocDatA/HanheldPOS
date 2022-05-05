package com.hanheldpos.ui.screens.pincode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.PosApp
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.cashdrawer.CashDrawerStatusResp
import com.hanheldpos.data.api.pojo.employee.EmployeeResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.cashdrawer.CashDrawerRepo
import com.hanheldpos.data.repository.employee.EmployeeRepo
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cashdrawer.CashDrawerStatusReq
import com.hanheldpos.model.cashdrawer.DrawerStatus
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel
import com.hanheldpos.utils.GSonUtils
import java.util.*

class PinCodeVM : BaseRepoViewModel<EmployeeRepo, PinCodeUV>() {

    // Data
    private val lstResultLD = MutableLiveData<MutableList<String>?>(mutableListOf())
    private var displayClockState = MutableLiveData(false)

    private val cashDrawerRepo = CashDrawerRepo();

    val listSize: MutableLiveData<Int> =
        Transformations.map(lstResultLD) { listResult: List<String>? ->
            if (listResult != null) {
                return@map listResult.size
            } else {
                return@map -1
            }
        } as MutableLiveData<Int>


    fun backPress() {
        uiCallback?.goBack();
    }

    fun initNumberPadList(): List<PinCodeRecyclerElement> {
        val list: MutableList<PinCodeRecyclerElement> = mutableListOf();
        // Line 1
        list.add(PinCodeRecyclerElement("3", null))
        list.add(PinCodeRecyclerElement("2", null))
        list.add(PinCodeRecyclerElement("1", null))
        // Line 2
        list.add(PinCodeRecyclerElement("6", null))
        list.add(PinCodeRecyclerElement("5", null))
        list.add(PinCodeRecyclerElement("4", null))
        // Line 3
        list.add(PinCodeRecyclerElement("9", null))
        list.add(PinCodeRecyclerElement("8", null))
        list.add(PinCodeRecyclerElement("7", null))
        // Line 4
        list.add(PinCodeRecyclerElement(null, R.drawable.ic_baseline_arrow_back_l, false))
        list.add(PinCodeRecyclerElement("0", null, false))

        return list;
    }

    private fun addItem(item: Int) {
        if (lstResultLD.value != null) {
            lstResultLD.value!!.add(item.toString() + "")
            lstResultLD.notifyValueChange();
        }
        if (listSize.value != null && listSize.value == PIN_MAX_LENGTH) {
            checkPinInput()
        }
    }

    private fun removeItem() {
        if (!lstResultLD.value.isNullOrEmpty()) {
            lstResultLD.value!!.removeAt(lstResultLD.value!!.size.minus(1));
            lstResultLD.notifyValueChange();
        }
    }

    private fun checkPinInput() {
        if (lstResultLD.value != null && lstResultLD.value!!.size == PIN_MAX_LENGTH) {
            val passCodeBuilder = StringBuilder()

            for (i in 0 until PIN_MAX_LENGTH) {
                if (lstResultLD.value != null) {
                    passCodeBuilder.append(Objects.requireNonNull<List<String>?>(lstResultLD.value)[i])
                }
            }
            // Fetch data
            showLoading(true);
            fetchDataEmployee(passCodeBuilder.toString());

        }
    }

    private fun fetchDataEmployee(passCode: String) {
        val userGuid = UserHelper.getUserGuid();
        val locationGuid = UserHelper.getLocationGuid();
        repo?.getDataEmployee(
            userGuid,
            passCode,
            locationGuid,
            object : BaseRepoCallback<BaseResponse<List<EmployeeResp>>> {
                override fun apiResponse(data: BaseResponse<List<EmployeeResp>>?) {
                    if (data == null || data.DidError || data.Model.isNullOrEmpty()) {
                        showError(
                            data?.Message ?: data?.ErrorMessage
                            ?: PosApp.instance.getString(R.string.an_error_occur)
                        );
                        onEmployeeError()
                    } else {
                        onEmployeeSuccess(data.Model.first())
                    }
                }

                override fun showMessage(message: String?) {
                    showLoading(false)
                    showError(PosApp.instance.getString(R.string.failed_to_load_data))
                    onEmployeeError()
                }
            })
    }

    fun onEmployeeSuccess(result: EmployeeResp) {
        UserHelper.curEmployee = result

        if (displayClockState.value == true) {
            /*checkClockInOut()*/
        } else {
            checkDrawerStatus();
        }
        /*changeClockState()*/
    }

    fun onEmployeeError() {
        lstResultLD.value?.clear();
        lstResultLD.notifyValueChange();
    }

    fun onClick(item: PinCodeRecyclerElement?) {
        if (item?.resource != null) {
            removeItem()
            return
        }
        if (listSize.value != null && listSize.value!! < PIN_MAX_LENGTH) {
            for (i in 0..9) {
                if (item?.text == i.toString() + "") {
                    addItem(i)
                    return
                }
            }
        }
    }

    private fun checkDrawerStatus() {
        val body = GSonUtils.toServerJson(
            CashDrawerStatusReq(
                UserGuid = UserHelper.getUserGuid(),
                DeviceGuid = UserHelper.getDeviceGuid(),
                LocationGuid = UserHelper.getLocationGuid(),
                EmployeeGuid = UserHelper.getEmployeeGuid()
            )
        );

        cashDrawerRepo.getStatusCashDrawer(body, callback = object  : BaseRepoCallback<BaseResponse<List<CashDrawerStatusResp>>?>{
            override fun apiResponse(data: BaseResponse<List<CashDrawerStatusResp>>?) {
                showLoading(false)
                if (data == null || data.DidError || data.Model.isNullOrEmpty()) {
                    showError(data?.ErrorMessage ?:  "Have some error.");
                    lstResultLD.value?.clear();
                    lstResultLD.notifyValueChange();
                } else {
                    when(DrawerStatus.fromInt(data.Model.first().StatusId)) {
                        DrawerStatus.NOT_FOUND -> uiCallback?.goStartDrawer();
                        else->{
                            DataHelper.currentDrawerId = data.Model.first().CashDrawerGuid;
                            uiCallback?.goHome()
                        };
                    }
                }
            }

            override fun showMessage(message: String?) {
                showLoading(false)
                showError(message);
            }
        });

    }

    companion object {
        private const val PIN_MAX_LENGTH = 4
    }

    override fun createRepo(): EmployeeRepo {
        return EmployeeRepo();
    }


}