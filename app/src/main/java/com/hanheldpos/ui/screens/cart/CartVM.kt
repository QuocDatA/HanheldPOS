package com.hanheldpos.ui.screens.cart

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.setting.SettingDeviceResp
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.order.OrderAsyncRepo
import com.hanheldpos.data.repository.setting.SettingRepo
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.cart.CartConverter
import com.hanheldpos.model.cart.CartModel
import com.hanheldpos.model.cart.DiscountCart
import com.hanheldpos.model.cart.payment.PaymentStatus
import com.hanheldpos.model.order.Order
import com.hanheldpos.model.order.OrderModel
import com.hanheldpos.model.order.OrderStatus
import com.hanheldpos.model.order.OrderSubmitResp
import com.hanheldpos.model.setting.SettingDevicePut
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.GSonUtils

class CartVM : BaseUiViewModel<CartUV>() {

    private val settingResp = SettingRepo();
    private val orderAlterRepo = OrderAsyncRepo();

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

        if (cart.paymentsList.isEmpty()) {
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
            cart.orderCode = DataHelper.generateOrderIdByFormat();

            if (DataHelper.ordersCompleted == null) {
                DataHelper.ordersCompleted = mutableListOf(
                    CartConverter.toOrder(
                        cart,
                        OrderStatus.COMPLETED.value,
                        PaymentStatus.PAID.value
                    )
                );
            } else {
                DataHelper.ordersCompleted = DataHelper.ordersCompleted.apply {
                    (this as MutableList).add(
                        CartConverter.toOrder(
                            cart,
                            OrderStatus.COMPLETED.value,
                            PaymentStatus.PAID.value
                        )
                    );
                }
            }
            showLoading(false);
            AppAlertDialog.get()
                .show(
                    "Notification",
                    "Successful bill payment",
                );
            uiCallback?.onBillSuccess();
        } catch (ex: Exception) {
            showLoading(false)
            AppAlertDialog.get()
                .show(
                    "Notification",
                    "Bill payment failed!",
                );
        }


        // TODO : sync order
//        val json = GSonUtils.toServerJson(
//            SettingDevicePut(
//                MaxChar = DataHelper.numberIncreaseOrder.toString().length.toLong(),
//                NumberIncrement = DataHelper.numberIncreaseOrder.toString(),
//                UserGuid = UserHelper.getUserGui(),
//                LocationGuid = UserHelper.getLocationGui(),
//                DeviceGuid = UserHelper.getDeviceGui(),
//                Device_key = DataHelper.getDeviceByDeviceCode()?._key!!.toString()
//            )
//        );
//        settingResp.putSettingDeviceIds(
//            json,
//            callback = object : BaseRepoCallback<SettingDeviceResp> {
//                override fun apiResponse(data: SettingDeviceResp?) {
//                    if (data == null || data.DidError) {
//                        AppAlertDialog.get()
//                            .show(
//                                "Error",
//                                data?.ErrorMessage,
//                            )
//                        showLoading(true)
//                    } else {
//                        val orderJson = GSonUtils.toServerJson(
//                            CartConverter.toOrder(
//                                cart,
//                                OrderStatus.COMPLETED.value,
//                                PaymentStatus.PAID.value
//                            )
//                        );
//                        orderAlterRepo.postOrderSubmit(orderJson, callback = object :
//                            BaseRepoCallback<OrderSubmitResp> {
//                            override fun apiResponse(data: OrderSubmitResp?) {
//                                showLoading(false);
//                                if (data == null || data.Message?.contains("exist") == true) {
//                                    AppAlertDialog.get()
//                                        .show(
//                                            "Notification",
//                                            data?.Message ?: "Push order failed",
//                                        );
//                                } else {
//                                    AppAlertDialog.get()
//                                        .show(
//                                            "Notification",
//                                            "Push order succeeded",
//                                        );
//                                    uiCallback?.onBillSuccess();
//                                }
//                            }
//
//                            override fun showMessage(message: String?) {
//                                showLoading(false);
//                                AppAlertDialog.get()
//                                    .show(
//                                        "Error",
//                                        message,
//                                    )
//                            }
//                        })
//
//                        print(orderJson);
//                    }
//                }
//
//                override fun showMessage(message: String?) {
//                    showLoading(false);
//                    showError(message);
//                }
//            });
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