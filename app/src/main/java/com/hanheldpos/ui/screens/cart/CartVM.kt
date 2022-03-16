package com.hanheldpos.ui.screens.cart

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.hanheldpos.R
import com.hanheldpos.database.DatabaseMapper
import com.hanheldpos.model.DatabaseHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.cart.CartConverter
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.cart.DiscountCart
import com.hanheldpos.model.cart.payment.PaymentStatus
import com.hanheldpos.model.home.table.TableStatusType
import com.hanheldpos.model.order.OrderStatus
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.time.DateTimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class CartVM : BaseUiViewModel<CartUV>() {

    fun backPress() {
        uiCallback?.getBack()
    }

    fun deleteCart() {
        uiCallback?.deleteCart()
    }

    fun openDiscount() {
        uiCallback?.onOpenDiscount()
    }

    fun openSelectPayment(payable: Double) {
        uiCallback?.openSelectPayment(false, payable)
    }

    fun onOpenAddCustomer() {
        uiCallback?.onOpenAddCustomer()
    }

    fun onShowCustomerDetail() {
        uiCallback?.onShowCustomerDetail()
    }

    fun billCart(context: Context, cart: CartModel, listener: CartActionCallBack) {
        if (cart.productsList.isEmpty()) {
            AppAlertDialog.get()
                .show(
                    context.getString(R.string.notification),
                    context.getString(R.string.order_not_completed),
                )
            return
        }
        onOrderProcessing(context, cart, listener)
    }

    private fun onOrderProcessing(context: Context, cart: CartModel, listener: CartActionCallBack) {
        showLoading(true)
        try {
            // Update cart object
            if (cart.orderCode == null)
                cart.orderCode = OrderHelper.generateOrderIdByFormat()
            if (cart.orderGuid == null)
                cart.orderGuid = cart.orderCode
            if (cart.createDate == null)
                cart.createDate =
                    DateTimeUtils.dateToString(
                        Date(),
                        DateTimeUtils.Format.FULL_DATE_UTC_TIMEZONE
                    )
            val orderStatus =
                if (OrderHelper.isPaymentSuccess(cart)) OrderStatus.COMPLETED.value else OrderStatus.ORDER.value
            val paymentStatus =
                if (OrderHelper.isPaymentSuccess(cart)) PaymentStatus.PAID.value else PaymentStatus.UNPAID.value
            val orderReq = CartConverter.toOrder(
                cart,
                orderStatus,
                paymentStatus,
            )

            // Table
            val table = CurCartData.tableFocus!!
            if (orderStatus == OrderStatus.ORDER.value && paymentStatus == PaymentStatus.UNPAID.value)
                table.updateTableStatus(TableStatusType.Unavailable, orderReq.OrderSummary)
            else
                table.updateTableStatus(TableStatusType.Available)

            viewModelScope.launch(Dispatchers.IO) {
                val orderEntity = DatabaseHelper.ordersCompleted.get(orderReq.Order.Code!!)
                if (orderEntity == null)
                    DatabaseHelper.ordersCompleted.insert(
                        DatabaseMapper.mappingOrderCompletedReqToEntity(orderReq)
                    ) else {
                    DatabaseHelper.ordersCompleted.update(
                        DatabaseMapper.mappingOrderCompletedReqToEntity(orderReq)
                    )
                }
                if (orderStatus != OrderStatus.COMPLETED.value && paymentStatus != PaymentStatus.PAID.value) {
                    DatabaseHelper.tableStatuses.insert(DatabaseMapper.mappingTableToEntity(table))
                    launch(Dispatchers.Main) { listener.onTableChange() }
                } else {
                    DatabaseHelper.tableStatuses.delete(table._Id)
                    launch(Dispatchers.Main) { listener.onTableChange() }
                }

                launch(Dispatchers.Main) {
                    showLoading(false)
                    uiCallback?.onBillSuccess()
                }
            }


        } catch (ex: Exception) {
            showLoading(false)
            AppAlertDialog.get()
                .show(
                    context.getString(R.string.notification),
                    context.getString(R.string.bill_payment_failed),
                )
        }

    }


    fun processDataDiscount(cart: CartModel): List<DiscountCart> {
        val list = mutableListOf<DiscountCart>()

        cart.discountUserList.forEach {
            list.add(DiscountCart(it, it.DiscountName, it.total(cart.getSubTotal())))
        }
        cart.compReason?.let {
            list.add(DiscountCart(it, it.Title!!, cart.totalComp(cart.totalTemp())))
        }
        return list
    }

    interface CartActionCallBack {
        fun onTableChange()
    }

}