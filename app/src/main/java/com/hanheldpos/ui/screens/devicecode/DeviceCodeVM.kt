package com.hanheldpos.ui.screens.devicecode

import android.content.Context
import android.os.SystemClock
import android.provider.Settings.Global.getString
import androidx.lifecycle.MutableLiveData
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.fee.FeeResp
import com.hanheldpos.data.api.pojo.order.menu.OrderMenuResp
import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.device.DeviceCodeResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.payment.PaymentsResp
import com.hanheldpos.data.api.pojo.table.TableResp
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.device.DeviceRepo
import com.hanheldpos.data.repository.discount.DiscountRepo
import com.hanheldpos.data.repository.fee.FeeRepo
import com.hanheldpos.data.repository.floor.FloorRepo
import com.hanheldpos.data.repository.menu.MenuRepo
import com.hanheldpos.data.repository.order.OrderRepo
import com.hanheldpos.data.repository.payment.PaymentRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseRepoViewModel
import java.util.*

class DeviceCodeVM : BaseRepoViewModel<DeviceRepo, DeviceCodeUV>() {

    val pinGroupSize = 4;
    val pinTextLD = MutableLiveData<String>();
    var context : Context? = null;

    private var mLastTimeClick: Long = 0;

    private var menuRepo : MenuRepo = MenuRepo();
    private var orderRepo: OrderRepo = OrderRepo();
    private var floorRepo: FloorRepo = FloorRepo();
    private var feeRepo: FeeRepo = FeeRepo();
    private var discountRepo : DiscountRepo = DiscountRepo();
    private var paymentRepo : PaymentRepo = PaymentRepo();

    override fun createRepo(): DeviceRepo {
        return DeviceRepo();
    }

    fun initContext(context : Context) {
        this.context = context;
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
                onDataFailure(message);
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
                onDataFailure(it.message);

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
                        onDataFailure(context?.getString(R.string.failed_to_load_data));
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
                    if (data == null || data.didError == true) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data));
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
                    if (data == null || data.didError == true) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data));
                    } else {
                        DataHelper.tableResp = data;
                        startMappingData();
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message);
                }
            });

        feeRepo.getFees( userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<FeeResp?> {
                override fun apiResponse(data: FeeResp?) {
                    if (data == null || data.didError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data));
                    } else {
                        DataHelper.feeResp = data;
                        startMappingData();
                    }
                }

                override fun showMessage(message: String?) {
                }

            },);

        discountRepo.getDiscountList(userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<DiscountResp> {
                override fun apiResponse(data: DiscountResp?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data));
                    } else {
                        DataHelper.discountResp = data;
                        startMappingData();
                    }
                }

                override fun showMessage(message: String?) {
                }

            },);
        paymentRepo.getPaymentMethods(userGuid = userGuid,callback = object : BaseRepoCallback<PaymentsResp>{
            override fun apiResponse(data: PaymentsResp?) {
                if (data == null || data.DidError) {
                    onDataFailure(context?.getString(R.string.failed_to_load_data));
                } else {
                    DataHelper.paymentsResp = data;
                    startMappingData();
                }
            }

            override fun showMessage(message: String?) {

            }
        })
    }


    fun onDataFailure(message: String?) {
        DataHelper.clearData();
        uiCallback?.showMessage(message);
    }

    private fun startMappingData() {
        DataHelper.let {
            it.orderMenuResp?:return;
            it.tableResp?:return;
            it.feeResp?:return;
            it.discountResp?:return;
            it.paymentsResp?:return;
        }
        uiCallback?.openPinCode();
    }

    fun backPress() {
        uiCallback?.goBack();
    }




}