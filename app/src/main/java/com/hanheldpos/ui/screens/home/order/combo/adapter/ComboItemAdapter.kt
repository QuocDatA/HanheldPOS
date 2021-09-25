package com.hanheldpos.ui.screens.home.order.combo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemOrderProductBinding
import com.hanheldpos.model.home.order.combo.ComboItemActionType
import com.hanheldpos.model.home.order.menu.ComboPickedItemViewModel
import com.hanheldpos.model.product.ProductOrderItem
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class ComboItemAdapter(
    private val listener: ComboItemListener
) : BaseBindingListAdapter<ComboPickedItemViewModel>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_order_product;
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBindingViewHolder<ComboPickedItemViewModel> {
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent, false
        ).also {
            Log.d("OrderProductAdapter","RecycleView Height:" + parent.height);
            val height = parent.resources.getDimension(R.dimen._75sdp);
            val params : FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,height.toInt());
            (it as ItemOrderProductBinding).layoutMain.layoutParams = params;
            return BaseBindingViewHolder(it)
        }
    }

    override fun onBindViewHolder(
        holder: BaseBindingViewHolder<ComboPickedItemViewModel>,
        position: Int
    ) {
        val item = getItem(position);
        val binding = holder.binding as ItemOrderProductBinding;
        binding.item = item.selectedComboItem;
        binding.root.setOnClickListener {
            listener.onComboItemChoose(action = ComboItemActionType.Add,item);
        }
    }

    interface ComboItemListener {
        fun onComboItemChoose(action: ComboItemActionType, item : ComboPickedItemViewModel)
    }

    class DiffCallback : DiffUtil.ItemCallback<ComboPickedItemViewModel>() {
        override fun areItemsTheSame(
            oldItem: ComboPickedItemViewModel,
            newItem: ComboPickedItemViewModel
        ): Boolean {
            return oldItem.selectedComboItem?.id == newItem.selectedComboItem?.id;
        }

        override fun areContentsTheSame(
            oldItem: ComboPickedItemViewModel,
            newItem: ComboPickedItemViewModel
        ): Boolean {
            return false
        }

    }
}