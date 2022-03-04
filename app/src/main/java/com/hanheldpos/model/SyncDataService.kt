package com.hanheldpos.model

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.hanheldpos.PosApp
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.discount.CouponResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.FeeResp
import com.hanheldpos.data.api.pojo.floor.FloorResp
import com.hanheldpos.data.api.pojo.order.menu.MenuResp
import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.resource.ResourceResp
import com.hanheldpos.data.api.pojo.system.AddressTypeResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.discount.DiscountRepo
import com.hanheldpos.data.repository.fee.FeeRepo
import com.hanheldpos.data.repository.floor.FloorRepo
import com.hanheldpos.data.repository.menu.MenuRepo
import com.hanheldpos.data.repository.order.OrderRepo
import com.hanheldpos.data.repository.payment.PaymentRepo
import com.hanheldpos.data.repository.resource.ResourceRepo
import com.hanheldpos.data.repository.system.SystemRepo
import com.hanheldpos.ui.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyncDataService : BaseViewModel() {

    private var menuRepo: MenuRepo = MenuRepo();
    private var orderRepo: OrderRepo = OrderRepo();
    private var floorRepo: FloorRepo = FloorRepo();
    private var feeRepo: FeeRepo = FeeRepo();
    private var discountRepo: DiscountRepo = DiscountRepo();
    private var paymentRepo: PaymentRepo = PaymentRepo();
    private var systemRepo: SystemRepo = SystemRepo();
    private var resourceRepo: ResourceRepo = ResourceRepo()

    fun fetchAllData(context: Context?, listener: SyncDataServiceListener) {
        val location = UserHelper.getLocationGuid()
        val userGuid = UserHelper.getUserGuid()
        menuRepo.getOrderMenu(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<BaseResponse<MenuResp>> {
                override fun apiResponse(data: BaseResponse<MenuResp>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data), listener);
                    } else {
                        DataHelper.menuLocalStorage = data.Model;
                        startMappingData(context, listener);
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener);
                }
            });

        orderRepo.getOrderSetting(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<BaseResponse<List<OrderSettingResp>>?> {
                override fun apiResponse(data: BaseResponse<List<OrderSettingResp>>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data), listener);
                    } else {
                        DataHelper.orderSettingLocalStorage = data.Model?.firstOrNull();
                        startMappingData(context, listener);
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener);
                }
            }
        )
        floorRepo.getPosFloor(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<BaseResponse<List<FloorResp>>?> {
                override fun apiResponse(data: BaseResponse<List<FloorResp>>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data), listener);
                    } else {
                        DataHelper.floorLocalStorage = data.Model?.firstOrNull();
                        startMappingData(context, listener);
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener);
                }
            });

        feeRepo.getFees(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<BaseResponse<FeeResp>?> {
                override fun apiResponse(data: BaseResponse<FeeResp>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data), listener);
                    } else {
                        DataHelper.feeLocalStorage = data.Model;
                        startMappingData(context, listener);
                    }
                }

                override fun showMessage(message: String?) {

                    onDataFailure(message, listener);
                }

            },
        );

        discountRepo.getDiscountList(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<BaseResponse<List<DiscountResp>>> {
                override fun apiResponse(data: BaseResponse<List<DiscountResp>>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data), listener);
                    } else {
                        DataHelper.discountsLocalStorage = data.Model;
                        startMappingData(context, listener);
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener);
                }
            },
        );

        discountRepo.getDiscountDetailList(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<BaseResponse<List<CouponResp>>> {
                override fun apiResponse(data: BaseResponse<List<CouponResp>>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data), listener);
                    } else {
                        DataHelper.discountDetailsLocalStorage = data.Model;
                        startMappingData(context, listener);
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener);
                }
            },
        );

        paymentRepo.getPaymentMethods(userGuid = userGuid, callback = object :
            BaseRepoCallback<BaseResponse<List<PaymentMethodResp>>> {
            override fun apiResponse(data: BaseResponse<List<PaymentMethodResp>>?) {
                if (data == null || data.DidError) {
                    onDataFailure(context?.getString(R.string.failed_to_load_data), listener);
                } else {
                    DataHelper.paymentMethodsLocalStorage = data.Model;
                    startMappingData(context, listener);
                }
            }

            override fun showMessage(message: String?) {
                onDataFailure(message, listener);
            }
        })

        systemRepo.getAddressTypes(callback = object :
            BaseRepoCallback<BaseResponse<List<AddressTypeResp>>> {
            override fun apiResponse(data: BaseResponse<List<AddressTypeResp>>?) {
                if (data == null || data.DidError) {
                    onDataFailure(context?.getString(R.string.failed_to_load_data), listener);
                } else {
                    DataHelper.addressTypesLocalStorage = data.Model;
                    startMappingData(context, listener);
                }
            }

            override fun showMessage(message: String?) {
                onDataFailure(message, listener)
            }

        })

        resourceRepo.getResource(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<BaseResponse<List<ResourceResp>>?> {
                override fun apiResponse(data: BaseResponse<List<ResourceResp>>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context?.getString(R.string.failed_to_load_data), listener)
                    } else {
                        DataHelper.resourceLocalStorage = data.Model;
                        startMappingData(context, listener);
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener)
                }

            }
        );
    }

    private fun onDataFailure(message: String?, listener: SyncDataServiceListener) {
        DataHelper.clearData()
        listener.onError(message)
    }


    private fun startMappingData(context: Context?, listener: SyncDataServiceListener) {
        DataHelper.let {
            it.menuLocalStorage ?: return;
            it.floorLocalStorage ?: return;
            it.feeLocalStorage ?: return;
            it.discountsLocalStorage ?: return;
            it.discountDetailsLocalStorage ?: return;
            it.paymentMethodsLocalStorage ?: return;
            it.addressTypesLocalStorage ?: return;
            it.resourceLocalStorage ?: return;
        }
        DataHelper.numberIncreaseOrder =
            DataHelper.deviceCodeLocalStorage?.SettingsId?.firstOrNull()?.NumberIncrement?.toLong()
                ?: 0
        var isNeedToDownload = false
        DataHelper.resourceLocalStorage?.forEach { resourceResp ->
            if (!DownloadService.checkFileExist(resourceResp.Name)) {
                isNeedToDownload = true
                DownloadService.initDownloadService(context!!)
                DownloadService.downloadFile(
                    DataHelper.resourceLocalStorage!!,
                    listener = object : DownloadService.DownloadFileCallback {
                        override fun onDownloadStartOrResume() {

                        }

                        override fun onPause() {

                        }

                        override fun onCancel() {
                            onDataFailure(context.getString(R.string.failed_to_load_data),listener)
                        }


                        override fun onFail() {
                            onDataFailure(context.getString(R.string.failed_to_load_data),listener)
                        }

                        override fun onComplete() {
                            listener.onLoadedResources();
                        }
                    })
                return
            }
        }

        if(!isNeedToDownload){
            listener.onLoadedResources()
        }
    }

    interface SyncDataServiceListener {
        fun onLoadedResources();
        fun onError(message: String?);
    }
}