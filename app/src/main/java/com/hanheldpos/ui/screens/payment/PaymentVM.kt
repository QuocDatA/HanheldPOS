package com.hanheldpos.ui.screens.payment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hanheldpos.data.api.pojo.payment.GiftCardResp
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.data.api.pojo.payment.PaymentSuggestionItem
import com.hanheldpos.data.api.pojo.payment.WalletCardResp
import com.hanheldpos.data.repository.BaseResponse
import com.hanheldpos.data.repository.base.BaseRepoCallback
import com.hanheldpos.data.repository.payment.PaymentRepo
import com.hanheldpos.database.DatabaseMapper
import com.hanheldpos.extension.notifyValueChange
import com.hanheldpos.model.DataHelper
import com.hanheldpos.model.DatabaseHelper
import com.hanheldpos.model.UserHelper
import com.hanheldpos.model.payment.CardNumberReqModel
import com.hanheldpos.model.payment.PaymentOrder
import com.hanheldpos.model.payment.method.BaseCardPayment
import com.hanheldpos.model.payment.method.BasePayment
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel
import com.hanheldpos.ui.screens.cart.CurCartData
import com.hanheldpos.utils.GSonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.parcelize.RawValue
import java.lang.Exception

class PaymentVM : BaseUiViewModel<PaymentUV>() {
    private val paymentRepo = PaymentRepo()
    val balance = MutableLiveData<Double>()
    val listPaymentChosen = MutableLiveData(mutableListOf<PaymentOrder>())

    val totalPay = Transformations.map(listPaymentChosen) {
        return@map it.sumOf { paymentOrder ->
            paymentOrder.OverPay ?: 0.0
        }
    }

    fun backPress() {
        uiCallback?.getBack()
    }

    fun getPaymentMethods(): MutableList<PaymentMethodResp> {
        return DataHelper.paymentMethodsLocalStorage!!.toMutableList()
    }

    fun initPaymentSuggestion(): List<PaymentSuggestionItem> {
        return mutableListOf(
            PaymentSuggestionItem(50000.0),
            PaymentSuggestionItem(100000.0),
            PaymentSuggestionItem(250000.0),
            PaymentSuggestionItem(500000.0),
        )
    }

    fun completedPayment(alreadyBill: Boolean, callback: PaymentFragment.PaymentCallback) {
        uiCallback?.onFragmentBackPressed()
        callback.onPaymentComplete(
            listPaymentChosen.value!!
        )
        if (alreadyBill) callback.onPayment(true)
    }

    fun addPaymentChosen(payment: PaymentOrder) {
        CoroutineScope(Dispatchers.IO).launch {
            listPaymentChosen.value?.apply {
                this.add(payment)
                val orderEntity =
                    DatabaseHelper.ordersCompleted.get(CurCartData.cartModel?.orderCode ?: "")
                if (orderEntity != null) {
                    val order = DatabaseMapper.mappingOrderReqFromEntity(orderEntity)
                    (order.OrderDetail.PaymentList as MutableList).add(payment)
                    DatabaseHelper.ordersCompleted.update(
                        DatabaseMapper.mappingOrderCompletedReqToEntity(order)
                    )
                } else {
                    throw Exception("")
                }

            }
            launch(Dispatchers.Main) {
                listPaymentChosen.notifyValueChange()
            }

        }

    }

    fun processPaymentGiftCard(payment: BaseCardPayment, cardNumber: String) {
        showLoading(true)
        paymentRepo.getValidGiftCard(
            UserHelper.getUserGuid(),
            cardNumber,
            callback = object : BaseRepoCallback<BaseResponse<GiftCardResp>> {
                override fun apiResponse(data: BaseResponse<GiftCardResp>?) {
                    showLoading(false)
                    if (data == null || data.DidError) {
                        showError(data?.ErrorMessage)
                    } else {
                        uiCallback?.onValidCardNumber(payment, cardNumber, data.Model?.Balance)
                    }
                }

                override fun showMessage(message: String?) {
                    showLoading(false)
                    showError(message)
                }
            })
    }

    fun processPaymentWalletCard(payment: BaseCardPayment, keyWallet: String) {
        showLoading(true)
        paymentRepo.getValidWallet(
            keyWallet,
            callback = object : BaseRepoCallback<BaseResponse<WalletCardResp>> {
                override fun apiResponse(data: BaseResponse<WalletCardResp>?) {
                    showLoading(false)
                    if (data == null || data.DidError) {
                        showError(data?.Message)
                    } else {
                        uiCallback?.onValidCardNumber(payment, keyWallet, data.Model?.Balance)
                    }
                }

                override fun showMessage(message: String?) {
                    showLoading(false)
                    showError(message)
                }
            })
    }

    fun syncPaymentCard(
        base: BaseCardPayment,
        orderGuid: String,
        cardCode: String,
        amount: Double,
        balance: Double,
        customerId: String?
    ) {
        showLoading(true)
        val cardNumberReq = CardNumberReqModel(
            UserGuid = UserHelper.getUserGuid(),
            EmployeeGuid = UserHelper.getEmployeeGuid(),
            DeviceGuid = UserHelper.getDeviceGuid(),
            CustomerGuid = customerId,
            OrderGuid = orderGuid,
            LocationGuid = UserHelper.getLocationGuid(),
            CardCode = cardCode,
            MethodId = base.paymentMethod._id,
            ApplyToId = base.paymentMethod.ApplyToId,
            Payable = base.getPayable(amount, balance),
        )

        paymentRepo.syncPaymentCard(
            GSonUtils.toServerJson(cardNumberReq),
            callback = object : BaseRepoCallback<BaseResponse<@RawValue Any>> {
                override fun apiResponse(data: BaseResponse<@RawValue Any>?) {
                    showLoading(false)
                    if (data == null || data.DidError) {
                        showError(data?.Message ?: data?.ErrorMessage)
                    } else {
                        uiCallback?.paymentChosenSuccess(base, amount)
                    }
                }

                override fun showMessage(message: String?) {
                    showLoading(false)
                    showError(message)
                }
            })
    }

    fun openPaymentDetail() {
        uiCallback?.openPaymentDetail()
    }

}