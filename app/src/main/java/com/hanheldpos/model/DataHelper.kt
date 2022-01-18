package com.hanheldpos.model


import com.google.gson.reflect.TypeToken
import com.hanheldpos.data.api.ApiConst
import com.hanheldpos.data.api.pojo.device.DeviceCodeResp
import com.hanheldpos.data.api.pojo.discount.CouponResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.fee.FeeResp
import com.hanheldpos.data.api.pojo.floor.FloorResp
import com.hanheldpos.data.api.pojo.order.menu.MenuResp
import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.model.cart.fee.FeeApplyToType
import com.hanheldpos.prefs.PrefKey
import com.utils.helper.AppPreferences

object DataHelper {

    var CurrentDrawerId : String? = null;

    fun clearData() {
        deviceCode = null
        menu = null
        orderSetting = null
        floor = null
        fee = null
        discounts = null
        discountDetails = null
        paymentMethods = null
        CurrentDrawerId = null
        numberIncreaseOrder = 0;
        AppPreferences.get().storeValue(PrefKey.Setting.DEVICE_CODE, null)
    }

    fun generateOrderIdByFormat() : String {
        var numberIncrement : Long = numberIncreaseOrder;
        numberIncrement = numberIncrement.plus(1);
        numberIncreaseOrder = numberIncrement;
        val prefix = getDeviceCodeModel()?.SettingsId?.firstOrNull()?.Prefix ?: ""
        val deviceAcronymn =getDeviceCodeModel()?.Device?.firstOrNull()?.Acronymn ?: ""
        val minimumNumber = getDeviceCodeModel()?.SettingsId?.firstOrNull()?.MinimumNumber ?: 0
        return "${prefix}${deviceAcronymn}${numberIncrement.toString().padEnd(minimumNumber, '0')}";
    }

    //region ## Order Menu

    var menu: MenuResp? = null
        get() {
            if (field == null) {
                field = StorageHelper.getDataFromEncryptedFile(
                    PrefKey.Order.MENU_RESP,
                    classOff =  MenuResp::class.java
                )
            }
            return field
        }
        set(value) {
            field = value
            StorageHelper.setDataToEncryptedFile(PrefKey.Order.MENU_RESP, value)
        }

    private fun getGroupsOrderMenu() = menu?.GroupList;

    fun findGroupNameOrderMenu(group_id : String) : String{
        return getGroupsOrderMenu()?.firstOrNull { groupsItem -> groupsItem._Id == group_id }?.GroupName ?: ""
    }

    //endregion
    //region ### Order Settings
    var orderSetting: OrderSettingResp? = null
        get() {
            if (field == null) {
                field = StorageHelper.getDataFromEncryptedFile(
                    PrefKey.Order.MENU_SETTING_RESP,
                    classOff =  OrderSettingResp::class.java
                )
            }
            return field
        }
        set(value) {
            field = value
            StorageHelper.setDataToEncryptedFile(PrefKey.Order.MENU_SETTING_RESP, value)
        }

    fun getVoidInfo() = orderSetting?.ListVoid?.firstOrNull()

    fun getVoidList() = getVoidInfo()?.ListReasons

    fun getVoidItemById(voidId: Int) = getVoidList()?.find { it.Id == voidId }

    private fun getCompInfo() = orderSetting?.ListComp?.firstOrNull()

    fun getCompList() = getCompInfo()?.ListReasons

    fun getCompItemById(voidId: Int) = getCompList()?.find { it.Id == voidId }

    fun getDiningOptionList() = orderSetting?.ListDiningOptions

    fun getDiningOptionItem(diningOptionId: Int) =
        getDiningOptionList()?.find { it.Id == diningOptionId }


    fun getDefaultDiningOptionItem() = getDiningOptionList()?.firstOrNull()

    //endregion
    //region ## Device Code
    var deviceCode: DeviceCodeResp? = null
        get() {
            if (field == null) {
                field = StorageHelper.getDataFromEncryptedFile(
                    PrefKey.Setting.DEVICE_CODE,
                    classOff =  DeviceCodeResp::class.java
                )
            }
            return field
        }
        set(value) {
            field = value
            StorageHelper.setDataToEncryptedFile(PrefKey.Setting.DEVICE_CODE, value)
        }

    private fun getDeviceCodeModel() = deviceCode;

    fun getDeviceCodeEmployee() = getDeviceCodeModel()?.EmployeeList;

    fun getDeviceByDeviceCode() = getDeviceCodeModel()?.Device?.firstOrNull()

    fun getCurrencySymbol() = getDeviceCodeModel()?.Users?.CurrencySymbol

    fun getDeviceGuidByDeviceCode() = getDeviceByDeviceCode()?._Id

    fun getLocationGuidByDeviceCode() = getDeviceByDeviceCode()?.Location

    fun getUserGuidByDeviceCode() = getDeviceByDeviceCode()?.UserGuid

    //endregion

    //region ## TableStatus

    var floor: FloorResp? = null
        get() {
            if (field == null) {
                field = StorageHelper.getDataFromEncryptedFile(
                    PrefKey.Floor.FLOOR_RESP,
                    classOff =  FloorResp::class.java
                )
            }
            return field
        }
        set(value) {
            // TODO reInit floor table status - this should be done by the server
            value?.FloorTable?.forEach {
                it.Visible = ApiConst.VISIBLE
            }

            field = value

            StorageHelper.setDataToEncryptedFile(PrefKey.Floor.FLOOR_RESP, value)
        }

    //endregion


    //region ## Fee
    var fee: FeeResp? = null
        get() {
            if (field == null) {
                field = StorageHelper.getDataFromEncryptedFile(
                    PrefKey.Fee.FEE_RESP,
                    classOff =  FeeResp::class.java
                )
            }
            return field
        }
        set(value) {
            field = value
            StorageHelper.setDataToEncryptedFile(PrefKey.Fee.FEE_RESP, value)
        }

    private fun getListFee() : List<Fee>? = fee?.Fees

    /**
     * Get Fee type [FeeApplyToType] with product id
     */
    fun findFeeProductList(productId : String) : List<Fee>? {
        return getListFee()?.filter { fee->
            FeeApplyToType.fromInt(fee.Id) != FeeApplyToType.Order && fee.AssignToProductList.firstOrNull{ assign_p->
                assign_p.ProductGuid == productId
            } != null
        }?.toList()
    }
    /**
     * Get Fee type [FeeApplyToType] for order
     */
    fun findFeeOrderList() : List<Fee>? {
        return getListFee()?.filter { fee->
            FeeApplyToType.fromInt(fee.Id) != FeeApplyToType.Order
        }?.toList()
    }


    //endregion

    //region Discount

    var discounts: List<DiscountResp>? = null
        get() {
            if (field == null) {
                field = StorageHelper.getDataFromEncryptedFile(
                    PrefKey.Discount.DISCOUNT_RESP,
                    object : TypeToken<List<DiscountResp>>() {}.type
                )
            }
            return field
        }
        set(value) {
            field = value
            StorageHelper.setDataToEncryptedFile(PrefKey.Discount.DISCOUNT_RESP, value)
        }

    var discountDetails: List<CouponResp>? = null
        get() {
            if (field == null) {
                field = StorageHelper.getDataFromEncryptedFile(
                    PrefKey.Discount.DISCOUNT_DETAIL_RESP,
                    object : TypeToken<List<CouponResp>>() {}.type
                )
            }
            return field
        }
        set(value) {
            field = value
            StorageHelper.setDataToEncryptedFile(PrefKey.Discount.DISCOUNT_DETAIL_RESP, value)
        }

    //endregion

    //region Payment

    var paymentMethods : List<PaymentMethodResp>? = null
        get() {
            if (field == null) {
                field = StorageHelper.getDataFromEncryptedFile(
                    PrefKey.Payment.PAYMENTS_RESP,
                    object : TypeToken<List<PaymentMethodResp>>() {}.type
                )
            }
            return field
        }
        set(value) {
            field = value
            StorageHelper.setDataToEncryptedFile(PrefKey.Payment.PAYMENTS_RESP, value)
        }

    fun getPaymentMethodList()= this.paymentMethods

    //region Order Storage

    var numberIncreaseOrder : Long = 0
        get() {
            return AppPreferences.get().getLong(PrefKey.Order.FILE_NAME_NUMBER_INCREASEMENT)
        }
        set(value) {
            field = value
            AppPreferences.get().storeValue(PrefKey.Order.FILE_NAME_NUMBER_INCREASEMENT, value)
        }



    //endregion
}