package com.hanheldpos.ui.screens.devicecode

import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.data.api.pojo.fee.FeeResp
import com.hanheldpos.data.api.pojo.order.menu.OrderMenuResp
import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.setting.DeviceCodeResp
import com.hanheldpos.data.api.pojo.table.TableResp
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.device.DeviceRepo
import com.hanheldpos.data.repository.fee.FeeRepo
import com.hanheldpos.data.repository.floor.FloorRepo
import com.hanheldpos.data.repository.menu.MenuRepo
import com.hanheldpos.data.repository.order.OrderRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel
import java.util.*

class DeviceCodeVM : BaseRepoViewModel<DeviceRepo, DeviceCodeUV>() {

    val pinGroupSize = 4;
    val pinTextLD = MutableLiveData<String>();

    private var mLastTimeClick: Long = 0;

    private var menuRepo : MenuRepo = MenuRepo();
    private var orderRepo: OrderRepo = OrderRepo();
    private var floorRepo: FloorRepo = FloorRepo();
    private var feeResp: FeeRepo = FeeRepo();

    override fun createRepo(): DeviceRepo {
        return DeviceRepo();
    }

    fun signIn() {
        if (SystemClock.elapsedRealtime() - mLastTimeClick < 1000) return;
        mLastTimeClick = SystemClock.elapsedRealtime();
        uiCallback?.showLoading(true);
        val result = getPinWithSymbol(pinTextLD.value.toString());
        repo?.getDataByAppCode(result, object : BaseRepoCallback<DeviceCodeResp> {
            override fun apiResponse(data: DeviceCodeResp?) {
                onDataSuccess(data);
            }

            override fun showMessage(message: String?) {
                showError(message);
            }
        })
    }



    private fun getPinWithSymbol(pinTextStr: String): String {
        val charList = LinkedList(pinTextStr.toList())
        if (charList.size > 5)
            charList.add(4, '-')
        if (charList.size > 10)
            charList.add(9, '-')

        return charList.joinToString(separator = "")
    }

    private fun onDataSuccess(result: DeviceCodeResp?) {

        result?.let {
            if (it.didError == true) {
                showError(it.message ?: "There was not found");
            } else {
                DataHelper.deviceCodeResp = it;
                fetchAllData();
            }
        }
    }

    private fun fetchAllData() {
        val location = DataHelper.getLocationGuidByDeviceCode()
        val userGuid = DataHelper.getUserGuidByDeviceCode()
        menuRepo.getOrderMenu(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<OrderMenuResp?> {
                override fun apiResponse(data: OrderMenuResp?) {
                    if (data == null || data?.didError == true) {
                        onDataFailure("Failed to load data");
                    } else {
                        DataHelper.orderMenuResp = data;

                        startMappingData();
                    }
                }
                override fun showMessage(message: String?) {
                    onDataFailure(message);
                }
            });

        orderRepo.getOrderSetting(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<OrderSettingResp?>{
                override fun apiResponse(data: OrderSettingResp?) {
                    if (data == null || data?.didError == true) {
                        onDataFailure("Failed to load data");
                    } else {
                        DataHelper.orderSettingResp = data;
                        startMappingData();
                    }
                }
                override fun showMessage(message: String?) {
                    onDataFailure(message)
                }
            }
        )
        floorRepo.getPosFloor(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<TableResp?> {
                override fun apiResponse(data: TableResp?) {
                    if (data == null || data?.didError == true) {
                        onDataFailure("Failed to load data");
                    } else {
                        DataHelper.tableResp = data;
                        startMappingData();
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message);
                }
            });

        feeResp.getFees( userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<FeeResp?> {
                override fun apiResponse(data: FeeResp?) {
                    if (data == null || data.didError) {
                        onDataFailure("Failed to load data");
                    } else {
                        DataHelper.feeResp = data;
                        startMappingData();
                    }
                }

                override fun showMessage(message: String?) {
                }

            },);
    }


    fun onDataFailure(message: String?) {
        DataHelper.clearData();
        showError(message);
    }

    private fun startMappingData() {
        if (DataHelper.orderMenuResp != null
            && DataHelper.tableResp != null
            && DataHelper.feeResp!=null) {
            uiCallback?.openPinCode();
        }
    }

    fun backPress() {
        uiCallback?.goBack();
    }




}