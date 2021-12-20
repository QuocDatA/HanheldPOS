package com.hanheldpos.ui.screens.devicecode

import android.content.Context
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.device.DeviceCodeResp
import com.hanheldpos.data.api.pojo.discount.CouponResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.FeeResp
import com.hanheldpos.data.api.pojo.order.menu.OrderMenuResp
import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.payment.PaymentsResp
import com.hanheldpos.data.api.pojo.table.TableResp
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.discount.DiscountRepo
import com.hanheldpos.data.repository.fee.FeeRepo
import com.hanheldpos.data.repository.floor.FloorRepo
import com.hanheldpos.data.repository.menu.MenuRepo
import com.hanheldpos.data.repository.order.OrderRepo
import com.hanheldpos.data.repository.payment.PaymentRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.ui.base.viewmodel.BaseViewModel

class SyncDataService : BaseViewModel() {
    //var context : Context? = null;

    private var menuRepo: MenuRepo = MenuRepo();
    private var orderRepo: OrderRepo = OrderRepo();
    private var floorRepo: FloorRepo = FloorRepo();
    private var feeRepo: FeeRepo = FeeRepo();
    private var discountRepo: DiscountRepo = DiscountRepo();
    private var paymentRepo: PaymentRepo = PaymentRepo();
    fun fetchAllData(context: Context?, listener: SyncDataServiceListener) {
        val location = DataHelper.getLocationGuidByDeviceCode()
        val userGuid = DataHelper.getUserGuidByDeviceCode()
        menuRepo.getOrderMenu(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<OrderMenuResp?> {
                override fun apiResponse(data: OrderMenuResp?) {
                    if (data == null || data?.didError == true) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data),listener);
                    } else {
                        DataHelper.orderMenuResp = data;

                        startMappingData(listener);
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message,listener);
                }
            });

        orderRepo.getOrderSetting(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<OrderSettingResp?> {
                override fun apiResponse(data: OrderSettingResp?) {
                    if (data == null || data.didError == true) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data),listener);
                    } else {
                        DataHelper.orderSettingResp = data;
                        startMappingData(listener);
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message,listener);
                }
            }
        )
        floorRepo.getPosFloor(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<TableResp?> {
                override fun apiResponse(data: TableResp?) {
                    if (data == null || data.didError == true) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data),listener);
                    } else {
                        DataHelper.tableResp = data;
                        startMappingData(listener);
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message,listener);
                }
            });

        feeRepo.getFees(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<FeeResp?> {
                override fun apiResponse(data: FeeResp?) {
                    if (data == null || data.didError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data),listener);
                    } else {
                        DataHelper.feeResp = data;
                        startMappingData(listener);
                    }
                }

                override fun showMessage(message: String?) {

                    onDataFailure(message,listener);
                }

            },
        );

        discountRepo.getDiscountList(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<DiscountResp> {
                override fun apiResponse(data: DiscountResp?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data),listener);
                    } else {
                        DataHelper.discountResp = data;
                        startMappingData(listener);
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message,listener);
                }
            },
        );

        discountRepo.getDiscountDetailList(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<CouponResp> {
                override fun apiResponse(data: CouponResp?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data),listener);
                    } else {
                        DataHelper.discountDetailResp = data;
                        startMappingData(listener);
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message,listener);
                }
            },
        );

        paymentRepo.getPaymentMethods(userGuid = userGuid, callback = object :
            BaseRepoCallback<PaymentsResp> {
            override fun apiResponse(data: PaymentsResp?) {
                if (data == null || data.DidError) {
                    onDataFailure(context?.getString(R.string.failed_to_load_data),listener);
                } else {
                    DataHelper.paymentsResp = data;
                    startMappingData(listener);
                }
            }

            override fun showMessage(message: String?) {
                onDataFailure(message,listener);
            }
        })
    }

    private fun onDataFailure(message: String?,listener: SyncDataServiceListener) {
        DataHelper.clearData();
        showLoading(false);
        listener.onError(message);
    }


    private fun startMappingData(listener: SyncDataServiceListener) {
        DataHelper.let {
            it.orderMenuResp ?: return;
            it.tableResp ?: return;
            it.feeResp ?: return;
            it.discountResp ?: return;
            it.discountDetailResp ?: return;
            it.paymentsResp ?: return;
        }

        listener.onLoadedResources();

    }

    interface SyncDataServiceListener {
        fun onLoadedResources();
        fun onError(message: String?);
    }
}