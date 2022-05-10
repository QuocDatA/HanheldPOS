package com.hanheldpos.model


import com.hanheldpos.data.api.ApiConst
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.device.Device
import com.hanheldpos.data.api.pojo.device.DeviceCodeResp
import com.hanheldpos.data.api.pojo.discount.CouponResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.FeeResp
import com.hanheldpos.data.api.pojo.floor.FloorResp
import com.hanheldpos.data.api.pojo.order.menu.MenuResp
import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.resource.ResourceResp
import com.hanheldpos.data.api.pojo.system.AddressTypeResp
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.discount.DiscApplyTo
import com.hanheldpos.model.discount.DiscountTriggerType
import com.hanheldpos.prefs.PrefKey
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.utils.GSonUtils
import com.utils.helper.AppPreferences
import java.util.*

object DataHelper {

    var currentDrawerId: String? = null;

    fun clearData() {
        currentDrawerId = null
        numberIncreaseOrder = 0
        // Local
        deviceCodeLocalStorage = null
        menuLocalStorage = null
        orderSettingLocalStorage = null
        floorLocalStorage = null
        feeLocalStorage = null
        discountsLocalStorage = null
        discountDetailsLocalStorage = null
        paymentMethodsLocalStorage = null
    }

    var numberIncreaseOrder: Long = 0
        get() {
            return AppPreferences.get().getLong(PrefKey.Order.FILE_NAME_NUMBER_INCREASEMENT)
        }
        set(value) {
            field = value
            AppPreferences.get().storeValue(PrefKey.Order.FILE_NAME_NUMBER_INCREASEMENT, value)
        }

    var menuLocalStorage: MenuResp? = null
        get() {
            if (field == null) {
                field = AppPreferences.get().getParcelableObject(
                    PrefKey.Order.MENU_RESP,
                    MenuResp::class.java
                )
            }
            return field
        }
        set(value) {
            field = value
            AppPreferences.get().storeValue(PrefKey.Order.MENU_RESP, value)
        }

    var orderSettingLocalStorage: OrderSettingResp? = null
        get() {
            if (field == null) {
                field = AppPreferences.get().getParcelableObject(
                    PrefKey.Order.MENU_SETTING_RESP,
                    OrderSettingResp::class.java
                )
            }
            return field
        }
        set(value) {
            field = value
            AppPreferences.get().storeValue(PrefKey.Order.MENU_SETTING_RESP, value)
        }

    var deviceCodeLocalStorage: DeviceCodeResp? = null
        get() {
            if (field == null) {
                field = AppPreferences.get().getParcelableObject(
                    PrefKey.Setting.DEVICE_CODE,
                    DeviceCodeResp::class.java
                )
            }
            return field
        }
        set(value) {
            field = value
            AppPreferences.get().storeValue(PrefKey.Setting.DEVICE_CODE, value)
        }

    var floorLocalStorage: FloorResp? = null
        get() {
            if (field == null) {
                field = AppPreferences.get().getParcelableObject(
                    PrefKey.Floor.FLOOR_RESP,
                    FloorResp::class.java
                )
            }
            return field
        }
        set(value) {
            value?.FloorTable?.forEach {
                it.Visible = ApiConst.VISIBLE
            }
            field = value
            AppPreferences.get().storeValue(PrefKey.Floor.FLOOR_RESP, value)
        }

    var feeLocalStorage: FeeResp? = null
        get() {
            if (field == null) {
                field = AppPreferences.get().getParcelableObject(
                    PrefKey.Fee.FEE_RESP,
                    FeeResp::class.java
                )
            }
            return field
        }
        set(value) {
            field = value
            AppPreferences.get().storeValue(PrefKey.Fee.FEE_RESP, value)
        }

    var discountsLocalStorage: List<DiscountResp>? = null
        get() {
            if (field == null) {
                field = GSonUtils.toList(
                    AppPreferences.get().getString(PrefKey.Discount.DISCOUNT_RESP)
                );
            }
            return field
        }
        set(value) {
            field = value
            AppPreferences.get().storeValue(PrefKey.Discount.DISCOUNT_RESP, GSonUtils.toJson(value))
        }

    var discountDetailsLocalStorage: List<CouponResp>? = null
        get() {
            if (field == null) {
                field = GSonUtils.toList(
                    AppPreferences.get().getString(PrefKey.Discount.DISCOUNT_DETAIL_RESP)
                )
            }
            return field
        }
        set(value) {
            field = value
            AppPreferences.get()
                .storeValue(PrefKey.Discount.DISCOUNT_DETAIL_RESP, GSonUtils.toJson(value))
        }

    var paymentMethodsLocalStorage: List<PaymentMethodResp>? = null
        get() {
            if (field == null) {
                field =
                    GSonUtils.toList(AppPreferences.get().getString(PrefKey.Payment.PAYMENTS_RESP))
            }
            return field
        }
        set(value) {
            field = value
            AppPreferences.get().storeValue(PrefKey.Payment.PAYMENTS_RESP, GSonUtils.toJson(value))
        }

    var addressTypesLocalStorage: List<AddressTypeResp>? = null
        get() {
            if (field == null) {
                field =
                    GSonUtils.toList(AppPreferences.get().getString(PrefKey.System.ADDRESS_TYPE))
            }
            return field
        }
        set(value) {
            field = value
            AppPreferences.get().storeValue(PrefKey.System.ADDRESS_TYPE, GSonUtils.toJson(value))
        }
    var resourceLocalStorage: List<ResourceResp>? = null
        get() {
            if (field == null) {
                field =
                    GSonUtils.toList(AppPreferences.get().getString(PrefKey.Resource.RESOURCE_RESP))
            }
            return field
        }
        set(value) {
            field = value
            AppPreferences.get().storeValue(PrefKey.Resource.RESOURCE_RESP, GSonUtils.toJson(value))
        }
    var recentDeviceCodeLocalStorage: List<Device>? = null
        get() {
            if (field == null) {
                field = GSonUtils.toList(
                    AppPreferences.get().getString(
                        PrefKey.Setting.RECENT_DEVICE_LIST,
                    )
                )
            }
            return field
        }
        set(value) {
            field = value
            AppPreferences.get()
                .storeValue(PrefKey.Setting.RECENT_DEVICE_LIST, GSonUtils.toJson(value))
        }

    fun findDiscountAutoList(applyTo: DiscApplyTo): List<DiscountResp> {
        return discountsLocalStorage?.filter { disc ->
            disc.DiscountAutomatic && disc.DiscountApplyTo == applyTo.value
        }?.toList() ?: listOf()
    }

    fun findDiscountItemList(
        baseProductInCart: BaseProductInCart?,
        customer: CustomerResp?,
        triggerType: DiscountTriggerType,
        timerServer: Date
    ): List<DiscountResp> {
        if (baseProductInCart == null) return listOf()
        return findDiscountAutoList(DiscApplyTo.ITEM).filter { discount ->
            discount.isValid(
                CurCartData.cartModel?.getSubTotal() ?: 0.0,
                baseProductInCart,
                customer,
                timerServer
            ) && discount.isExistsTrigger(triggerType)
        }.toList()
    }

    fun  findDiscountOrderList(
        cart: CartModel,
        timeServer: Date,
        triggerType: DiscountTriggerType = DiscountTriggerType.ALL
    ): List<DiscountResp> {
        return findDiscountAutoList(DiscApplyTo.ORDER).filter { discount ->
            discount.DiscountAutomatic && discount.isValid(
                cart,
                timeServer
            ) && discount.isExistsTrigger(triggerType)
        }.toList()
    }

    fun findDiscount(discountId: String): DiscountResp? {
        return discountsLocalStorage?.firstOrNull{discount -> discount._id == discountId}?.clone()
    }

    fun userGuid(): String {
        return deviceCodeLocalStorage?.Device?.firstOrNull()?.UserGuid ?: ""
    }

    fun locationGuid(): String {
        return deviceCodeLocalStorage?.Device?.firstOrNull()?.Location!!
    }

    fun deviceGuid(): String{
        return deviceCodeLocalStorage?.Device?.firstOrNull()?._Id!!
    }
}