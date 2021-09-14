package com.hanheldpos.ui.screens.pincode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.employee.EmployeeResp
import com.hanheldpos.data.repository.GDataResp
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.employee.EmployeeRepo
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.UserHelper

import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import okhttp3.internal.notify
import java.util.*

class PinCodeVM : BaseRepoViewModel<EmployeeRepo,PinCodeUV>() {

    // Data
    private val lstResultLD = MutableLiveData<MutableList<String>?>(mutableListOf())
    var displayClockState = MutableLiveData(false)

    val listSize: MutableLiveData<Int> = Transformations.map(lstResultLD) { listResult: List<String>? ->
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
        list.add(PinCodeRecyclerElement(null, R.drawable.ic_baseline_arrow_back_l,false))
        list.add(PinCodeRecyclerElement("0", null,false))

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

    private fun fetchDataEmployee(passCode : String){
        val userGuid = DataHelper.getUserGuidByDeviceCode();
        val locationGuid = DataHelper.getLocationGuidByDeviceCode();
        if (passCode != null && userGuid != null && locationGuid != null) {
            repo?.getDataEmployee(userGuid,passCode,locationGuid,object : BaseRepoCallback<GDataResp<EmployeeResp>>{
                override fun apiResponse(data: GDataResp<EmployeeResp>?) {
                    if (data == null || data.didError == true  || data.model.isNullOrEmpty()) {
                        showError(data?.message);
                        lstResultLD.value?.clear();
                        lstResultLD.notifyValueChange();
                    } else {
                        onEmployeeSuccess(data.model.first())
                    }
                }

                override fun showMessage(message: String?) {

                }
            })
        }
    }

    fun onEmployeeSuccess(result: EmployeeResp){
        uiCallback?.showLoading(false)


//        val model = result.model
        if (result.token != null) {
            UserHelper.curEmployee = result

            if (displayClockState.value == true) {
                /*checkClockInOut()*/
            } else {
                uiCallback?.goHome()
            }
        }
        /*changeClockState()*/
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


    companion object {
        private const val PIN_MAX_LENGTH = 4
    }

    override fun createRepo(): EmployeeRepo {
        return EmployeeRepo();
    }


}