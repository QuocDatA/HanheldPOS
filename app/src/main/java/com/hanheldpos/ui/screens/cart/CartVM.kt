package com.hanheldpos.ui.screens.cart

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.hanheldpos.R
import com.hanheldpos.database.DatabaseMapper
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.DatabaseHelper
import com.hanheldpos.model.OrderHelper
import com.hanheldpos.model.cart.CartConverter
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.cart.DiscountCart
import com.hanheldpos.model.cart.payment.PaymentStatus
import com.hanheldpos.model.order.OrderStatus
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartVM : BaseUiViewModel<CartUV>() {


    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
    }

    fun backPress() {
        uiCallback?.getBack();
    }

    fun deleteCart() {
        uiCallback?.deleteCart();
    }

    fun openDiscount() {
        uiCallback?.onOpenDiscount();
    }

    fun openSelectPayment(payable: Double) {
        uiCallback?.openSelectPayment(payable);
    }

    fun onOpenAddCustomer() {
        uiCallback?.onOpenAddCustomer();
    }

    fun onShowCustomerDetail() {
        uiCallback?.onShowCustomerDetail();
    }

    fun billCart(context: Context, cart: CartModel) {

        if (cart.productsList.isEmpty()) {
            AppAlertDialog.get()
                .show(
                    context.getString(R.string.notification),
                    context.getString(R.string.order_not_completed),
                )
            return;
        }

        if (cart.paymentsList.isEmpty() && cart.compReason == null) {
            AppAlertDialog.get()
                .show(
                    context.getString(R.string.notification),
                    context.getString(R.string.please_choose_payment_method),
                )
            return;
        }
        onOrderProcessing(cart);

    }

    private fun onOrderProcessing(cart: CartModel) {
        showLoading(true)
        try {
            cart.orderCode = OrderHelper.generateOrderIdByFormat();
            viewModelScope.launch(Dispatchers.IO) {
                DatabaseHelper.ordersCompleted.insert(
                    DatabaseMapper.mappingOrderCompletedReqToEntity(
                        CartConverter.toOrder(
                            cart,
                            OrderStatus.COMPLETED.value,
                            PaymentStatus.PAID.value
                        )
                    )
                )
                launch(Dispatchers.Main) {
                    showLoading(false);
                    uiCallback?.onBillSuccess();
                    AppAlertDialog.get()
                        .show(
                            "Notification",
                            "Successful bill payment",
                        );
                }
            }


        } catch (ex: Exception) {
            showLoading(false)
            AppAlertDialog.get()
                .show(
                    "Notification",
                    "Bill payment failed!",
                );
        }

    }


    fun processDataDiscount(cart: CartModel): List<DiscountCart> {
        val list = mutableListOf<DiscountCart>();

        cart.discountUserList.forEach {
            list.add(DiscountCart(it, it.DiscountName, it.total(cart.getSubTotal())));
        }
        cart.compReason?.let {
            list.add(DiscountCart(it, it.Title!!, cart.totalComp(cart.totalTemp())));
        }
        return list;
    }


}