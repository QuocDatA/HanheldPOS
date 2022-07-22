package com.hanheldpos.ui.screens.payment.completed

import androidx.lifecycle.MutableLiveData
import com.hanheldpos.PosApp
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.customer.CustomerResp
import com.hanheldpos.data.api.pojo.loyalty.LoyaltyResp
import com.hanheldpos.ui.base.viewmodel.BaseUiViewModel

class PaymentCompletedVM : BaseUiViewModel<PaymentCompletedUV>() {
    val customer = MutableLiveData<CustomerResp>()
    val loyaltyResp = MutableLiveData<LoyaltyResp>()

    fun getCustomerEarnedPoint(): String {
        return PosApp.instance.getString(R.string.congratulations_you_earned) + " " +
        (loyaltyResp.value?.receivable?.toInt()).toString() + " " + PosApp.instance.getString(R.string.points)
    }

    fun getCustomerGroupName(): String {
        return customer.value?.getCustomerListGroup()?.first()?.groupName ?: ""
    }

    fun getProgress(): Int {
        return loyaltyResp.value?.receivable?.toInt() ?: 0
    }

    fun getPoint(): String {
        return loyaltyResp.value?.receivable?.toInt().toString()
    }
}