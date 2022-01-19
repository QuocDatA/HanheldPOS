package com.hanheldpos.ui.screens.devicecode

import android.content.Context
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.CouponResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.FeeResp
import com.hanheldpos.data.api.pojo.floor.FloorResp
import com.hanheldpos.data.api.pojo.order.menu.MenuResp
import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.repository.BaseResponse
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
            callback = object : BaseRepoCallback<BaseResponse<MenuResp>> {
                override fun apiResponse(data: BaseResponse<MenuResp>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data),listener);
                    } else {
                        DataHelper.menu = data.Model;

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
            callback = object : BaseRepoCallback<BaseResponse<List<OrderSettingResp>>?> {
                override fun apiResponse(data: BaseResponse<List<OrderSettingResp>>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data),listener);
                    } else {
                        DataHelper.orderSetting = data.Model.firstOrNull();
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
            callback = object : BaseRepoCallback<BaseResponse<List<FloorResp>>?> {
                override fun apiResponse(data: BaseResponse<List<FloorResp>>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data),listener);
                    } else {
                        DataHelper.floor = data.Model.firstOrNull();
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
            callback = object : BaseRepoCallback<BaseResponse<FeeResp>?> {
                override fun apiResponse(data: BaseResponse<FeeResp>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data),listener);
                    } else {
                        DataHelper.fee = data.Model;
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
            callback = object : BaseRepoCallback<BaseResponse<List<DiscountResp>>> {
                override fun apiResponse(data: BaseResponse<List<DiscountResp>>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data),listener);
                    } else {
                        DataHelper.discounts = data.Model;
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
            callback = object : BaseRepoCallback<BaseResponse<List<CouponResp>>> {
                override fun apiResponse(data: BaseResponse<List<CouponResp>>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data),listener);
                    } else {
                        DataHelper.discountDetails = data.Model;
                        startMappingData(listener);
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message,listener);
                }
            },
        );

        paymentRepo.getPaymentMethods(userGuid = userGuid, callback = object :
            BaseRepoCallback<BaseResponse<List<PaymentMethodResp>>> {
            override fun apiResponse(data: BaseResponse<List<PaymentMethodResp>>?) {
                if (data == null || data.DidError) {
                    onDataFailure(context?.getString(R.string.failed_to_load_data),listener);
                } else {
                    DataHelper.paymentMethods = data.Model;
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
        listener.onError(message);
    }


    private fun startMappingData(listener: SyncDataServiceListener) {
        DataHelper.let {
            it.menu ?: return;
            it.floor ?: return;
            it.fee ?: return;
            it.discounts ?: return;
            it.discountDetails ?: return;
            it.paymentMethods ?: return;
        }
        DataHelper.numberIncreaseOrder = DataHelper.deviceCode?.SettingsId?.firstOrNull()?.NumberIncrement?.toLong() ?: 0;
        listener.onLoadedResources();

    }

    interface SyncDataServiceListener {
        fun onLoadedResources();
        fun onError(message: String?);
    }
}