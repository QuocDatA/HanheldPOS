package com.hanheldpos.ui.screens.cart.payment.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.payment.PaymentMethodResp
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class PaymentMethodAdapter(
    private val onPaymentMethodClickListener: BaseItemClickListener<PaymentMethodResp>,
) : BaseBindingListAdapter<PaymentMethodResp>(
    DiffCallback(),
    itemClickListener = onPaymentMethodClickListener,
) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_payment_method
    }

    private class DiffCallback : DiffUtil.ItemCallback<PaymentMethodResp>() {

        override fun areItemsTheSame(
            oldItem: PaymentMethodResp,
            newItem: PaymentMethodResp,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PaymentMethodResp,
            newItem: PaymentMethodResp
        ): Boolean {
            return false
        }

    }
}
