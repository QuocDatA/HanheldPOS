package com.hanheldpos.model


import com.hanheldpos.data.api.ApiConst
import com.hanheldpos.data.api.pojo.device.DeviceCodeResp
import com.hanheldpos.data.api.pojo.discount.CouponResp
import com.hanheldpos.data.api.pojo.discount.DiscountResp
import com.hanheldpos.data.api.pojo.employee.EmployeeResp
import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.data.api.pojo.fee.FeeResp
import com.hanheldpos.data.api.pojo.order.menu.OrderMenuResp
import com.hanheldpos.data.api.pojo.order.settings.OrderSettingResp
import com.hanheldpos.data.api.pojo.payment.PaymentsResp
import com.hanheldpos.data.api.pojo.table.TableResp
import com.hanheldpos.model.cart.fee.FeeApplyToType
import com.hanheldpos.prefs.PrefKey
import com.utils.helper.AppPreferences

object DataHelper {

    var CurrentDrawer_id : String? = null;

    fun clearData() {
        deviceCodeResp = null
        orderMenuResp = null
        orderSettingResp = null
        tableResp = null
        feeResp = null
        discountResp = null
        discountDetailResp = null
        paymentsResp = null
        CurrentDrawer_id = null
        numberIncreaseOrder = 0;
        AppPreferences.get().storeValue(PrefKey.Setting.DEVICE_CODE, null)
    }

    fun generateOrderIdByFormat() : String {
        var numberIncrement : Long = if (getDeviceCodeModel()?.SettingsId?.firstOrNull()?.NumberIncrement?.toLong() ?: 0 > numberIncreaseOrder) getDeviceCodeModel()?.SettingsId?.firstOrNull()?.NumberIncrement?.toLong() ?: 0 else numberIncreaseOrder;
        numberIncrement = numberIncrement.plus(1);
        numberIncreaseOrder = numberIncrement;
        val prefix = getDeviceCodeModel()?.SettingsId?.firstOrNull()?.Prefix ?: ""
        val deviceAcronymn =getDeviceCodeModel()?.Device?.firstOrNull()?.Acronymn ?: ""
        val minimumNumber = getDeviceCodeModel()?.SettingsId?.firstOrNull()?.MinimumNumber ?: 0
        return "${prefix}${deviceAcronymn}${numberIncrement.toString().padEnd(minimumNumber, '0')}";
    }

    //region ## Order Menu

    var orderMenuResp: OrderMenuResp? = null
        get() {
            if (field == null) {
                field = StorageHelper.getDataFromEncryptedFile(
                    PrefKey.Order.ORDER_MENU_RESP,
                    OrderMenuResp::class.java
                )
            }
            return field
        }
        set(value) {
            field = value
            StorageHelper.setDataToEncryptedFile(PrefKey.Order.ORDER_MENU_RESP, value)
        }

    private fun getOrderMenuModel() = orderMenuResp?.model?.firstOrNull()
    private fun getGroupsOrderMenu() = getOrderMenuModel()?.groups

    fun findGroupNameOrderMenu(group_id : String) : String{
        return getGroupsOrderMenu()?.firstOrNull { groupsItem ->  groupsItem?.id.equals(group_id)}?.groupName ?: ""
    }

    //endregion
    //region ### Order Settings
    var orderSettingResp: OrderSettingResp? = null
        get() {
            if (field == null) {
                field = StorageHelper.getDataFromEncryptedFile(
                    PrefKey.Order.ORDER_SETTING_RESP,
                    OrderSettingResp::class.java
                )
            }
            return field
        }
        set(value) {
            field = value
            StorageHelper.setDataToEncryptedFile(PrefKey.Order.ORDER_SETTING_RESP, value)
        }

    private fun getOrderSettingModel() = orderSettingResp?.model?.firstOrNull()

    fun getVoidInfo() = getOrderSettingModel()?.listVoid?.firstOrNull()

    fun getVoidList() = getVoidInfo()?.listReasons

    fun getVoidItemById(voidId: Int) = getVoidList()?.find { it?.id == voidId }

    private fun getCompInfo() = getOrderSettingModel()?.listComp?.firstOrNull()

    fun getCompList() = getCompInfo()?.listReasons

    fun getCompItemById(voidId: Int) = getCompList()?.find { it?.id == voidId }

    fun getDiningOptionList() = getOrderSettingModel()?.diningOptions

    fun getDiningOptionItem(diningOptionId: Int) =
        getDiningOptionList()?.find { it?.id == diningOptionId }


    fun getDefaultDiningOptionItem() = getDiningOptionList()?.firstOrNull()

    //endregion
    //region ## Device Code
    var deviceCodeResp: DeviceCodeResp? = null
        get() {
            if (field == null) {
                field = StorageHelper.getDataFromEncryptedFile(
                    PrefKey.Setting.DEVICE_CODE,
                    DeviceCodeResp::class.java
                )
            }
            return field
        }
        set(value) {
            field = value
            StorageHelper.setDataToEncryptedFile(PrefKey.Setting.DEVICE_CODE, value)
        }

    private fun getDeviceCodeModel() = deviceCodeResp;

    fun getDeviceCodeEmployee() = getDeviceCodeModel()?.EmployeeList;

    fun getDeviceByDeviceCode() = getDeviceCodeModel()?.Device?.firstOrNull()

    fun getCurrencySymbol() = getDeviceCodeModel()?.Users?.CurrencySymbol

    fun getDeviceGuidByDeviceCode() = getDeviceByDeviceCode()?._Id

    fun getLocationGuidByDeviceCode() = getDeviceByDeviceCode()?.Location

    fun getUserGuidByDeviceCode() = getDeviceByDeviceCode()?.UserGuid

    //endregion

    //region ## TableStatus

    var tableResp: TableResp? = null
        get() {
            if (field == null) {
                field = StorageHelper.getDataFromEncryptedFile(
                    PrefKey.Table.TABLE_RESP,
                    TableResp::class.java
                )
            }
            return field
        }
        set(value) {
            // TODO reInit floor table status - this should be done by the server
            value?.model?.firstOrNull()?.floorTable?.forEach {
                it?.visible = ApiConst.VISIBLE
            }

            field = value

            StorageHelper.setDataToEncryptedFile(PrefKey.Table.TABLE_RESP, value)
        }

    private fun getTableModel() = tableResp?.model?.firstOrNull()
    fun getTableStatus() = getTableModel()?.tableStatus

    //endregion


    //region ## Fee
    var feeResp: FeeResp? = null
        get() {
            if (field == null) {
                field = StorageHelper.getDataFromEncryptedFile(
                    PrefKey.Fee.FEE_RESP,
                    FeeResp::class.java
                )
            }
            return field
        }
        set(value) {
            field = value
            StorageHelper.setDataToEncryptedFile(PrefKey.Fee.FEE_RESP, value)
        }

    private fun getListFee() : List<Fee>? = feeResp?.feeModel?.fees

    /**
     * Get Fee type [FeeApplyToType] with product id
     */
    fun findFeeProductList(productId : String) : List<Fee>? {
        return getListFee()?.filter { fee->
            FeeApplyToType.fromInt(fee.feeApplyToType) != FeeApplyToType.Order && fee.assignToProducts.firstOrNull{ assign_p->
                assign_p.productId == productId
            } != null
        }?.toList()
    }
    /**
     * Get Fee type [FeeApplyToType] for order
     */
    fun findFeeOrderList() : List<Fee>? {
        return getListFee()?.filter { fee->
            FeeApplyToType.fromInt(fee.feeApplyToType) != FeeApplyToType.Order
        }?.toList()
    }


    //endregion

    //region Discount

    var discountResp: DiscountResp? = null
        get() {
            if (field == null) {
                field = StorageHelper.getDataFromEncryptedFile(
                    PrefKey.Discount.DISCOUNT_RESP,
                    DiscountResp::class.java
                )
            }
            return field
        }
        set(value) {
            field = value
            StorageHelper.setDataToEncryptedFile(PrefKey.Discount.DISCOUNT_RESP, value)
        }

    var discountDetailResp: CouponResp? = null
        get() {
            if (field == null) {
                field = StorageHelper.getDataFromEncryptedFile(
                    PrefKey.Discount.DISCOUNT_DETAIL_RESP,
                    CouponResp::class.java
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

    var paymentsResp : PaymentsResp? = null
        get() {
            if (field == null) {
                field = StorageHelper.getDataFromEncryptedFile(
                    PrefKey.Payment.PAYMENTS_RESP,
                    PaymentsResp::class.java
                )
            }
            return field
        }
        set(value) {
            field = value
            StorageHelper.setDataToEncryptedFile(PrefKey.Payment.PAYMENTS_RESP, value)
        }

    fun getPaymentMethodList()= this.paymentsResp?.Model

    //region Order Storage

    var numberIncreaseOrder : Long = 0
        get() {
            if (field == null) {
                field = AppPreferences.get().getLong(PrefKey.Order.FILE_NAME_NUMBER_INCREAMENT)
            }
            return field
        }
        set(value) {
            field = value
            AppPreferences.get().storeValue(PrefKey.Payment.PAYMENTS_RESP, value)
        }

    //endregion
}