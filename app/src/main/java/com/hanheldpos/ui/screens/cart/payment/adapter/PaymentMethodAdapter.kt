package com.hanheldpos.ui.screens.cart.payment.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemCartProductBinding
import com.hanheldpos.databinding.ItemPaymentMethodBinding
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.cart.payment.PaymentFragment.FakeMethodModel
import com.hanheldpos.ui.screens.pincode.PinCodeRecyclerElement
import com.hanheldpos.ui.screens.pincode.adapter.PinCodeAdapter

class PaymentMethodAdapter(
    private val onPaymentMethodClickListener: BaseItemClickListener<FakeMethodModel>,
) : BaseBindingListAdapter<FakeMethodModel>(
    DiffCallback(),
    itemClickListener = onPaymentMethodClickListener,
) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_payment_method;
    }

    class DiffCallback : DiffUtil.ItemCallback<FakeMethodModel>() {

        override fun areItemsTheSame(
            oldItem: FakeMethodModel,
            newItem: FakeMethodModel,
        ): Boolean {
            return oldItem == newItem;
        }

        override fun areContentsTheSame(
            oldItem: FakeMethodModel,
            newItem: FakeMethodModel
        ): Boolean {
            return false;
        }

    }
}
