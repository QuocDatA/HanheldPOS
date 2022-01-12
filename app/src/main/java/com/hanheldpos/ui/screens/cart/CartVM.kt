package com.hanheldpos.ui.screens.cart

import androidx.lifecycle.LifecycleOwner
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
import com.hanheldpos.model.order.OrderStatus
import com.hanheldpos.model.order.OrderSubmitResp
import com.hanheldpos.model.setting.SettingDevicePut
import com.hanheldpos.ui.base.dialog.AppAlertDialog
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.utils.JsonHelper

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

    fun openSelectPayment(payable : Double) {
        uiCallback?.openSelectPayment(payable);
    }

    fun onOpenAddCustomer() {
        uiCallback?.onOpenAddCustomer();
    }

    fun billCart(cart : CartModel) {
        if (cart.paymentsList.isEmpty()) {
            AppAlertDialog.get()
                .show(
                    "Warning",
                    "Please choose payment method",
                )
            return;
        }
        ;
        onOrderProcessing(cart);

    }

    private fun onOrderProcessing(cart : CartModel) {
        showLoading(true)
        cart.orderCode = DataHelper.generateOrderIdByFormat();
        val json = JsonHelper.stringify(
            SettingDevicePut(
            MaxChar = DataHelper.numberIncreaseOrder.toString().length.toLong(),
            NumberIncrement = DataHelper.numberIncreaseOrder.toString(),
            UserGuid = UserHelper.getUserGui(),
            LocationGuid = UserHelper.getLocationGui(),
            DeviceGuid = UserHelper.getDeviceGui(),
            Device_key = DataHelper.getDeviceByDeviceCode()?.key!!.toString()
        )
        );
        settingResp.putSettingDeviceIds(
            json,
            callback = object : BaseRepoCallback<SettingDeviceResp> {
                override fun apiResponse(data: SettingDeviceResp?) {
                    if (data == null || data.DidError) {
                        AppAlertDialog.get()
                            .show(
                                "Error",
                                data?.ErrorMessage,
                            )
                    } else {
                        val orderJson = JsonHelper.stringify(
                            CartConverter.toOrder(
                            cart,
                            OrderStatus.COMPLETED.value,
                            PaymentStatus.PAID.value
                        ));
                        orderAlterRepo.postOrderSubmit(orderJson, callback = object :
                            BaseRepoCallback<OrderSubmitResp> {
                            override fun apiResponse(data: OrderSubmitResp?) {
                                showLoading(false);
                                AppAlertDialog.get()
                                    .show(
                                        "Notification",
                                        "Push order success",
                                    );
                                uiCallback?.onBillSuccess();
                            }

                            override fun showMessage(message: String?) {
                                showLoading(false);
                                AppAlertDialog.get()
                                    .show(
                                        "Error",
                                        message,
                                    )
                            }
                        })

                        print(orderJson);
                    }
                }

                override fun showMessage(message: String?) {
                    showLoading(false);
                    showError(message);
                }
            });
    }



    fun processDataDiscount(cart: CartModel): List<DiscountCart> {
        val list = mutableListOf<DiscountCart>();

        cart.discountUserList.forEach {
            list.add(DiscountCart(it, it.DiscountName, it.total(cart.getSubTotal())));
        }
        cart.compReason?.let {
            list.add(DiscountCart(it, it.title!!, cart.totalComp(cart.totalTemp())));
        }
        return list;
    }

}