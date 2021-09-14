package com.hanheldpos.model

import com.hanheldpos.data.api.ApiConst
import com.hanheldpos.data.api.pojo.employee.EmployeeResp
import com.hanheldpos.data.api.pojo.order.OrderMenuResp
import com.hanheldpos.data.api.pojo.setting.devicecode.DeviceCodeResp
import com.hanheldpos.data.api.pojo.table.TableResp
import com.hanheldpos.prefs.PrefKey
import com.utils.helper.AppPreferences

object DataHelper {

    fun clearData() {
        deviceCodeResp = null
        orderMenuResp = null
        tableResp = null
        AppPreferences.get().storeValue(PrefKey.Setting.DEVICE_CODE, null)
    }

    //region ## Order Menu

    var orderMenuResp: OrderMenuResp? = null
        get() {
            if (field == null) {
                field = AppPreferences.get()
                    .getParcelableObject(PrefKey.Order.ORDER_MENU_RESP, OrderMenuResp::class.java)
            }
            return field
        }
        set(value) {
            field = value
            AppPreferences.get()
                .storeValue(PrefKey.Order.ORDER_MENU_RESP, value)
        }

    //endregion

    //region ## Device Code
    var deviceCodeResp: DeviceCodeResp? = null
        get() {
            if (field == null) {
                field = AppPreferences.get()
                    .getParcelableObject(PrefKey.Setting.DEVICE_CODE, DeviceCodeResp::class.java)
            }
            return field
        }
        set(value) {
            field = value
            AppPreferences.get().storeValue(PrefKey.Setting.DEVICE_CODE, value)
        }

    private fun getDeviceCodeModel() = deviceCodeResp?.model?.firstOrNull()

    fun getDeviceCodeEmployee() = getDeviceCodeModel()?.employees

    fun getDeviceByDeviceCode() = getDeviceCodeModel()?.device?.firstOrNull()

    fun getShowReceiptScreen() = getDeviceByDeviceCode()?.showReceiptScreen

    fun getStyleUI() = getDeviceByDeviceCode()?.styleUI
    fun getIsModifierNewPage() = getDeviceByDeviceCode()?.isModifierNewPage == 1
    fun getModifierStyle() = getDeviceByDeviceCode()?.modifierStyle

    fun getViewMode() = getDeviceCodeModel()?.viewItemMode?.firstOrNull()?.viewMode?.firstOrNull()

    fun getPictureMode() =
        getDeviceCodeModel()?.viewItemMode?.firstOrNull()?.pictureMode?.firstOrNull()

    fun getDeviceGuidByDeviceCode() = getDeviceByDeviceCode()?.id

    fun getLocationGuidByDeviceCode() = getDeviceByDeviceCode()?.location

    fun getUserGuidByDeviceCode() = getDeviceByDeviceCode()?.userGuid

    fun getEmployeeListByDeviceCode() = getDeviceCodeModel()?.employees


    fun getEmployeeByEmployeeGui(employeeGuid: String?): EmployeeResp? {
        if (employeeGuid == null) return null
        return getEmployeeListByDeviceCode()?.find { it?.id == employeeGuid }
    }

    fun isLocker() = getDeviceByDeviceCode()?.deviceType == 4

    private fun getListEmployeeByEmployeeGui(employeeGuidList: List<String>?): List<EmployeeResp>? {
        if (employeeGuidList.isNullOrEmpty()) return null
        return getEmployeeListByDeviceCode()?.filterNotNull()?.filter { it ->
            employeeGuidList.contains(it.id)
        }
    }

    //endregion

    //region ## TableStatus

    var tableResp: TableResp? = null
        get() {
            if (field == null) {
                field = AppPreferences.get()
                    .getParcelableObject(PrefKey.Table.TABLE_RESP, TableResp::class.java)
            }
            return field
        }
        set(value) {
            // TODO reInit floor table status - this should be done by the server
            value?.model?.firstOrNull()?.floorTable?.forEach {
                it?.visible = ApiConst.VISIBLE
            }

            field = value

            AppPreferences.get()
                .storeValue(PrefKey.Table.TABLE_RESP, value)
        }

    private fun getTableModel() = tableResp?.model?.firstOrNull()
    fun getTableStatus() = getTableModel()?.tableStatus

    //endregion
}