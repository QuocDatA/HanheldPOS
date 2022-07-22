package com.hanheldpos.model

import android.content.Context
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.data.DataVersion
import com.hanheldpos.data.api.pojo.discount.CouponResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.FeeResp
import com.hanheldpos.data.api.pojo.floor.FloorResp
import com.hanheldpos.data.api.pojo.order.menu.MenuResp
import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.order.status.OrderStatusResp
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.receipt.ReceiptCashier
import com.hanheldpos.data.api.pojo.resource.ResourceResp
import com.hanheldpos.data.api.pojo.setting.firebase.FirebaseSetting
import com.hanheldpos.data.api.pojo.setting.hardware.HardwareConnection
import com.hanheldpos.data.api.pojo.setting.hardware.HardwareSetting
import com.hanheldpos.data.api.pojo.system.AddressTypeResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.data.DataRepo
import com.hanheldpos.data.repository.discount.DiscountRepo
import com.hanheldpos.data.repository.fee.FeeRepo
import com.hanheldpos.data.repository.floor.FloorRepo
import com.hanheldpos.data.repository.menu.MenuRepo
import com.hanheldpos.data.repository.order.OrderRepo
import com.hanheldpos.data.repository.payment.PaymentRepo
import com.hanheldpos.data.repository.receipt.ReceiptRepo
import com.hanheldpos.data.repository.resource.ResourceRepo
import com.hanheldpos.data.repository.setting.SettingRepo
import com.hanheldpos.data.repository.system.SystemRepo
import com.hanheldpos.model.setting.GeneralSetting
import com.hanheldpos.ui.base.viewmodel.BaseViewModel

class SyncDataService : BaseViewModel() {


    private var menuRepo: MenuRepo = MenuRepo()
    private var orderRepo: OrderRepo = OrderRepo()
    private var floorRepo: FloorRepo = FloorRepo()
    private var feeRepo: FeeRepo = FeeRepo()
    private var discountRepo: DiscountRepo = DiscountRepo()
    private var paymentRepo: PaymentRepo = PaymentRepo()
    private var systemRepo: SystemRepo = SystemRepo()
    private var resourceRepo: ResourceRepo = ResourceRepo()
    private var receiptRepo: ReceiptRepo = ReceiptRepo()
    private var dataRepo: DataRepo = DataRepo()
    private var settingRepo: SettingRepo = SettingRepo()

    fun fetchAllData(context: Context, listener: SyncDataServiceListener) {
        val location = UserHelper.getLocationGuid()
        val userGuid = UserHelper.getUserGuid()
        val deviceGuid = UserHelper.getDeviceGuid()
        menuRepo.getOrderMenu(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<BaseResponse<MenuResp>> {
                override fun apiResponse(data: BaseResponse<MenuResp>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                    } else {
                        DataHelper.menuLocalStorage = data.Model
                        startMappingData(context, listener)
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener)
                }
            })

        orderRepo.getOrderSetting(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<BaseResponse<List<OrderSettingResp>>?> {
                override fun apiResponse(data: BaseResponse<List<OrderSettingResp>>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                    } else {
                        DataHelper.orderSettingLocalStorage = data.Model?.firstOrNull()
                        startMappingData(context, listener)
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener)
                }
            }
        )

        orderRepo.getOrderStatus(
            userGuid = userGuid,
            callback = object : BaseRepoCallback<BaseResponse<List<OrderStatusResp>>?> {
                override fun apiResponse(data: BaseResponse<List<OrderStatusResp>>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                    } else {
                        DataHelper.orderStatusLocalStorage = data.Model?.firstOrNull()
                        startMappingData(context, listener)
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener)
                }
            }
        )
        floorRepo.getPosFloor(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<BaseResponse<List<FloorResp>>?> {
                override fun apiResponse(data: BaseResponse<List<FloorResp>>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                    } else {
                        DataHelper.floorLocalStorage = data.Model?.firstOrNull()
                        startMappingData(context, listener)
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener)
                }
            })

        feeRepo.getFees(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<BaseResponse<FeeResp>?> {
                override fun apiResponse(data: BaseResponse<FeeResp>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                    } else {
                        DataHelper.feeLocalStorage = data.Model
                        startMappingData(context, listener)
                    }
                }

                override fun showMessage(message: String?) {

                    onDataFailure(message, listener)
                }

            },
        )

        discountRepo.getDiscountList(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<BaseResponse<List<DiscountResp>>> {
                override fun apiResponse(data: BaseResponse<List<DiscountResp>>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                    } else {
                        DataHelper.discountsLocalStorage = data.Model
                        startMappingData(context, listener)
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener)
                }
            },
        )

        discountRepo.getDiscountDetailList(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<BaseResponse<List<CouponResp>>> {
                override fun apiResponse(data: BaseResponse<List<CouponResp>>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                    } else {
                        DataHelper.discountDetailsLocalStorage = data.Model
                        startMappingData(context, listener)
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener)
                }
            },
        )

        paymentRepo.getPaymentMethods(userGuid = userGuid, callback = object :
            BaseRepoCallback<BaseResponse<List<PaymentMethodResp>>> {
            override fun apiResponse(data: BaseResponse<List<PaymentMethodResp>>?) {
                if (data == null || data.DidError) {
                    onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                } else {
                    DataHelper.paymentMethodsLocalStorage = data.Model
                    startMappingData(context, listener)
                }
            }

            override fun showMessage(message: String?) {
                onDataFailure(message, listener)
            }
        })

        systemRepo.getAddressTypes(callback = object :
            BaseRepoCallback<BaseResponse<List<AddressTypeResp>>> {
            override fun apiResponse(data: BaseResponse<List<AddressTypeResp>>?) {
                if (data == null || data.DidError) {
                    onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                } else {
                    DataHelper.addressTypesLocalStorage = data.Model
                    startMappingData(context, listener)
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
                        onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                    } else {
                        DataHelper.resourceLocalStorage = data.Model
                        startMappingData(context, listener)
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener)
                }

            }
        )

        receiptRepo.getReceiptCashiers(
            userGuid,
            location,
            callback = object : BaseRepoCallback<BaseResponse<List<ReceiptCashier>>> {
                override fun apiResponse(data: BaseResponse<List<ReceiptCashier>>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                    } else {
                        DataHelper.receiptCashierLocalStorage = data.Model?.firstOrNull()
                        startMappingData(context, listener)
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener)
                }
            })

        dataRepo.getDataVersion(
            userGuid,
            location,
            deviceGuid,
            callback = object : BaseRepoCallback<BaseResponse<DataVersion>> {
                override fun apiResponse(data: BaseResponse<DataVersion>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                    } else {
                        DataHelper.dataVersionLocalStorage = data.Model
                        startMappingData(context, listener)
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener)
                }
            })
        settingRepo.getFirebaseSetting(
            userGuid,
            location,
            deviceGuid,
            callback = object : BaseRepoCallback<BaseResponse<FirebaseSetting>> {
                override fun apiResponse(data: BaseResponse<FirebaseSetting>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                    } else {
                        DataHelper.firebaseSettingLocalStorage = data.Model
                        startMappingData(context, listener)
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener)
                }
            })
        settingRepo.getHardwareSetting(
            userGuid,
            location,
            deviceGuid,
            callback = object : BaseRepoCallback<BaseResponse<HardwareSetting>> {
                override fun apiResponse(data: BaseResponse<HardwareSetting>?) {
                    if (data == null || data.DidError) {
                        onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                    } else {
                        DataHelper.hardwareSettingLocalStorage = data.Model.apply {
                            this?.printerList?.forEach {
                                (it.connectionList as MutableList?)?.addAll(
                                    mutableListOf(
                                        HardwareConnection(
                                            3,
                                            "Connection/3",
                                            false,
                                            "Handheld Bluetooth",
                                            ""
                                        ),
                                        HardwareConnection(
                                            4,
                                            "Connection/4",
                                            false,
                                            "Handheld Urovo",
                                            ""
                                        )
                                    )
                                )
                            }
                        }
                        DataHelper.hardwareSettingLocalStorage = DataHelper.hardwareSettingLocalStorage
                        startMappingData(context, listener)
                    }
                }

                override fun showMessage(message: String?) {
                    onDataFailure(message, listener)
                }
            })

        DataHelper.generalSettingLocalStorage = GeneralSetting()
    }

    private fun onDataFailure(message: String?, listener: SyncDataServiceListener) {
        DataHelper.clearData()
        listener.onError(message)
    }

    private fun startMappingData(context: Context, listener: SyncDataServiceListener) {
        if (!DataHelper.isValidData()) return
        DataHelper.numberIncreaseOrder =
            DataHelper.deviceCodeLocalStorage?.ListSettingsId?.firstOrNull()?.NumberIncrement?.toLong()
                ?: 0
        var isNeedToDownload = false
        DataHelper.resourceLocalStorage?.let {
            if (it.isEmpty()) return@let
            isNeedToDownload = true
            DownloadService.downloadFile(context,
                it,
                listener = object : DownloadService.DownloadFileCallback {
                    override fun onDownloadStartOrResume() {

                    }

                    override fun onPause() {

                    }

                    override fun onCancel() {
                        onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                    }


                    override fun onFail(errorMessage: String?) {
                        if (errorMessage.isNullOrEmpty())
                            onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                        else onDataFailure(errorMessage, listener)
                    }

                    override fun onComplete() {
                        listener.onLoadedResources()
                    }
                })
            return
        }

        if (!isNeedToDownload) {
            listener.onLoadedResources()
        }
    }

    fun fetchMenuDiscountData(context: Context, listener: SyncDataServiceListener) {
        val location = UserHelper.getLocationGuid()
        val userGuid = UserHelper.getUserGuid()
        var succeededCount = 0
        menuRepo.getOrderMenu(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<BaseResponse<MenuResp>> {
                override fun apiResponse(data: BaseResponse<MenuResp>?) {
                    if (data == null || data.DidError || data.Model == null) {
                        listener.onError(
                            context.getString(R.string.failed_to_load_data)
                        )
                    } else {
                        DataHelper.menuLocalStorage = data.Model
                        succeededCount++
                        if (succeededCount >= 3) {
                            listener.onLoadedResources()
                        }
                    }
                }

                override fun showMessage(message: String?) {
                    listener.onError(message)
                }
            })
        discountRepo.getDiscountList(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<BaseResponse<List<DiscountResp>>> {
                override fun apiResponse(data: BaseResponse<List<DiscountResp>>?) {
                    if (data == null || data.DidError || data.Model == null) {
                        listener.onError(
                            context.getString(R.string.failed_to_load_data)
                        )
                    } else {
                        DataHelper.discountsLocalStorage = data.Model
                        succeededCount++
                        if (succeededCount >= 3) {
                            listener.onLoadedResources()
                        }
                    }
                }

                override fun showMessage(message: String?) {
                    listener.onError(message)
                }
            },
        )

        discountRepo.getDiscountDetailList(
            userGuid = userGuid,
            locationGuid = location,
            callback = object : BaseRepoCallback<BaseResponse<List<CouponResp>>> {
                override fun apiResponse(data: BaseResponse<List<CouponResp>>?) {
                    if (data == null || data.DidError || data.Model == null) {
                        listener.onError(
                            context.getString(R.string.failed_to_load_data),
                        )
                    } else {
                        DataHelper.discountDetailsLocalStorage = data.Model
                        succeededCount++
                        if (succeededCount >= 3) {
                            listener.onLoadedResources()
                        }
                    }
                }

                override fun showMessage(message: String?) {
                    listener.onError(message)
                }
            },
        )

    }

    fun checkNewUpdateVersion(context: Context, listener: SyncDataServiceListener) {
        val location = UserHelper.getLocationGuid()
        val userGuid = UserHelper.getUserGuid()
        val deviceGuid = UserHelper.getDeviceGuid()
        dataRepo.getDataVersion(
            userGuid,
            location,
            deviceGuid,
            callback = object : BaseRepoCallback<BaseResponse<DataVersion>> {
                override fun apiResponse(data: BaseResponse<DataVersion>?) {
                    if (data == null || data.DidError || data.Model == null) {
                        onDataFailure(context.getString(R.string.failed_to_load_data), listener)
                    } else {
                        if ((DataHelper.dataVersionLocalStorage?.menu ?: 0) < (data.Model.menu
                                ?: 0) || (DataHelper.dataVersionLocalStorage?.discount
                                ?: 0) < (data.Model.discount ?: 0)
                        ) {
                            listener.onLoadedResources(data.Model)

                        } else {
                            listener.onError(context.getString(R.string.no_new_update_found))
                        }

                    }
                }

                override fun showMessage(message: String?) {
                    listener.onError(message)
                }
            })
    }

    interface SyncDataServiceListener {
        fun onLoadedResources(data: Any? = null)
        fun onError(message: String?)
    }
}