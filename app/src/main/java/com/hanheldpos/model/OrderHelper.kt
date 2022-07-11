package com.hanheldpos.model

import com.hanheldpos.data.api.pojo.fee.Fee
import com.hanheldpos.database.entities.OrderCompletedEntity
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.cart.fee.FeeApplyToType
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.model.order.OrderStatus

object OrderHelper {
    fun findGroupNameOrderMenu(group_id: String): String {
        return DataHelper.menuLocalStorage?.GroupList?.firstOrNull { groupsItem -> groupsItem._Id == group_id }?.GroupName
            ?: ""
    }

    fun generateOrderIdByFormat(): String {
        var numberIncrement: Long = DataHelper.numberIncreaseOrder;
        numberIncrement = numberIncrement.plus(1);
        DataHelper.numberIncreaseOrder = numberIncrement;
        val prefix = DataHelper.deviceCodeLocalStorage?.ListSettingsId?.firstOrNull()?.Prefix ?: ""
        val deviceAcronymn =
            DataHelper.deviceCodeLocalStorage?.Device?.firstOrNull()?.Acronymn ?: ""
        val minimumNumber =
            DataHelper.deviceCodeLocalStorage?.ListSettingsId?.firstOrNull()?.MinimumNumber ?: 0
        return "${prefix}${deviceAcronymn}${numberIncrement.toString().padEnd(minimumNumber, '0')}";
    }

    fun getDiningOptionItem(diningOptionId: Int?) =
        DataHelper.orderSettingLocalStorage?.ListDiningOptions?.find { it.Id == diningOptionId }

    fun getDeviceCodeEmployee() = DataHelper.deviceCodeLocalStorage?.Employees;

    private fun getDeviceByDeviceCode() = DataHelper.deviceCodeLocalStorage?.Device?.firstOrNull()

    fun getCurrencySymbol() = DataHelper.deviceCodeLocalStorage?.Users?.CurrencySymbol

    fun getDeviceGuidByDeviceCode() = getDeviceByDeviceCode()?._Id

    fun getLocationGuidByDeviceCode() = getDeviceByDeviceCode()?.Location

    fun getUserGuidByDeviceCode() = getDeviceByDeviceCode()?.UserGuid

    fun findFeeProductList(productId: String): List<Fee>? {
        return DataHelper.feeLocalStorage?.Fees?.filter { fee ->
            FeeApplyToType.fromInt(fee.Id) != FeeApplyToType.Order && fee.AssignToProductList.firstOrNull { assign_p ->
                assign_p.ProductGuid == productId
            } != null
        }?.toList()
    }

    fun findFeeOrderList(): List<Fee>? {
        return DataHelper.feeLocalStorage?.Fees?.filter { fee ->
            FeeApplyToType.fromInt(fee.Id) == FeeApplyToType.Order
        }?.toList()
    }

    fun isPaymentSuccess(cart: CartModel): Boolean {
        val total = cart.getTotalPrice()
        val totalPay = cart.paymentsList?.sumOf {
            it.Payable ?: 0.0
        } ?: 0.0
        return totalPay >= total
    }

    fun isPaymentSuccess(orderModel: OrderModel): Boolean {
        val total = orderModel.OrderSummary.GrandTotal ?: 0.0
        val totalPay = orderModel.OrderDetail.PaymentList?.sumOf {
            it.Payable ?: 0.0
        }?: 0.0
        return totalPay >= total
    }

    fun isValidOrderPush(orderEntity: OrderCompletedEntity): Boolean {
        return (orderEntity.isSync != true) && orderEntity.statusId == OrderStatus.COMPLETED
    }

    fun isValidOrderLogout(orderEntity: OrderCompletedEntity):Boolean {
        return (orderEntity.isSync != true) || orderEntity.statusId == OrderStatus.ORDER
    }



}