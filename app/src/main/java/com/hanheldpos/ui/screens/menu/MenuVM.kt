package com.hanheldpos.ui.screens.menu

import android.content.Context
import com.hanheldpos.R
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.setting.SettingRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.menu.LogoutType
import com.hanheldpos.ui.screens.menu.adapter.ItemOptionNav
import com.hanheldpos.model.menu.NavBarOptionType
import com.hanheldpos.model.setting.SettingDevicePut
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.welcome.WelcomeFragment
import com.hanheldpos.utils.GSonUtils
import com.hanheldpos.utils.NetworkUtils
import com.hanheldpos.utils.StringUtils

class MenuVM : BaseUiViewModel<MenuUV>() {
    private val settingRepo = SettingRepo()

    fun backPress() {
        uiCallback?.getBack()
    }

    fun initMenuItemList(context: Context): List<ItemOptionNav> {
        return mutableListOf(
            ItemOptionNav(
                type = NavBarOptionType.DISCOUNT,
                name = getNameMenu(NavBarOptionType.DISCOUNT, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.ORDERS,
                name = getNameMenu(NavBarOptionType.ORDERS, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.TRANSACTIONS,
                name = getNameMenu(NavBarOptionType.TRANSACTIONS, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.REPORTS,
                name = getNameMenu(NavBarOptionType.REPORTS, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.CUSTOMER,
                name = getNameMenu(NavBarOptionType.CUSTOMER, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.ORDER_HISTORY,
                name = getNameMenu(NavBarOptionType.ORDER_HISTORY, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.SETTINGS,
                name = getNameMenu(NavBarOptionType.SETTINGS, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.SUPPORT,
                name = getNameMenu(NavBarOptionType.SUPPORT, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.UPDATE_DATA,
                name = getNameMenu(NavBarOptionType.UPDATE_DATA, context),
                value = DataHelper.isNeedToUpdateNewData.value
            ),
            ItemOptionNav(
                type = NavBarOptionType.LOGOUT_DEVICE,
                name = getNameMenu(NavBarOptionType.LOGOUT_DEVICE, context)
            ),
            ItemOptionNav(
                type = NavBarOptionType.RESET_SYSTEM,
                name = getNameMenu(NavBarOptionType.RESET_SYSTEM, context)
            ),
        )
    }

    private fun getNameMenu(type: NavBarOptionType, context: Context): String {
        return when (type) {
            NavBarOptionType.ORDERS -> context.getString(R.string.orders)
            NavBarOptionType.TRANSACTIONS -> context.getString(R.string.transactions)
            NavBarOptionType.REPORTS -> context.getString(R.string.reports)
            NavBarOptionType.CUSTOMER -> context.getString(R.string.customer)
            NavBarOptionType.ORDER_HISTORY -> context.getString(R.string.order_history)
            NavBarOptionType.SETTINGS -> context.getString(R.string.settings)
            NavBarOptionType.SUPPORT -> context.getString(R.string.support)
            NavBarOptionType.LOGOUT_DEVICE -> context.getString(R.string.logout_device)
            NavBarOptionType.RESET_SYSTEM -> context.getString(R.string.reset_system)
            NavBarOptionType.UPDATE_DATA -> context.getString(R.string.update_data)
            NavBarOptionType.DISCOUNT -> context.getString(R.string.discount)
        }
    }

    fun onLogoutDevice(context: Context, onPushSucceeded: () -> Unit) {
        val json = GSonUtils.toServerJson(
            SettingDevicePut(
                MaxChar = DataHelper.numberIncreaseOrder.toString().length.toLong(),
                NumberIncrement = DataHelper.numberIncreaseOrder.toString(),
                UserGuid = UserHelper.getUserGuid(),
                LocationGuid = UserHelper.getLocationGuid(),
                DeviceGuid = UserHelper.getDeviceGuid(),
                Device_key = DataHelper.deviceCodeLocalStorage?.Device?.firstOrNull()?._key!!.toString(),
                uuid = StringUtils.getAndroidDeviceId(context = context)
            )
        )
        settingRepo.putSettingDeviceIds(
            json,
            callback = object : BaseRepoCallback<BaseResponse<String>> {
                override fun apiResponse(data: BaseResponse<String>?) {
                    if (data == null || data.DidError) {
                        onPushSucceeded.invoke()
                    } else {
                        onPushSucceeded.invoke()
                    }
                }

                override fun showMessage(message: String?) {
                    onPushSucceeded.invoke()
                }
            })
    }
}