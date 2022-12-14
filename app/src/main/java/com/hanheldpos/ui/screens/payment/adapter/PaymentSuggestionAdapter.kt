package com.hanheldpos.ui.screens.payment.adapter


import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.data.api.pojo.payment.PaymentSuggestionItem
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class PaymentSuggestionAdapter(
    private val onPaymentSuggestionClickListener: BaseItemClickListener<PaymentSuggestionItem>,
) : BaseBindingListAdapter<PaymentSuggestionItem>(
    DiffCallback(),
    itemClickListener = onPaymentSuggestionClickListener,
) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_payment_suggestion
    }

    private class DiffCallback : DiffUtil.ItemCallback<PaymentSuggestionItem>() {
        override fun areItemsTheSame(
            oldItem: PaymentSuggestionItem,
            newItem: PaymentSuggestionItem,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PaymentSuggestionItem,
            newItem: PaymentSuggestionItem
        ): Boolean {
            return false
        }
    }
}