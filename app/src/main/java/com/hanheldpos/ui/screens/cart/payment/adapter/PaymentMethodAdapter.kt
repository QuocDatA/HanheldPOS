package com.hanheldpos.ui.screens.cart.payment.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.model.cart.payment.method.BasePayment
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class PaymentMethodAdapter(
    private val onPaymentMethodClickListener: BaseItemClickListener<BasePayment>,
) : BaseBindingListAdapter<BasePayment>(
    DiffCallback(),
    itemClickListener = onPaymentMethodClickListener,
) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_payment_method
    }

    private class DiffCallback : DiffUtil.ItemCallback<BasePayment>() {

        override fun areItemsTheSame(
            oldItem: BasePayment,
            newItem: BasePayment,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BasePayment,
            newItem: BasePayment
        ): Boolean {
            return false
        }

    }
}
