package com.hanheldpos.ui.screens.combo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.databinding.ItemComboRegularBinding
import com.hanheldpos.databinding.ItemOrderProductBinding
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.model.home.order.menu.ProductMenuItem
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener
import com.hanheldpos.ui.screens.cart.customer.adapter.CustomerAdapter

class ComboItemPickerAdapter(
    private val listener : BaseItemClickListener<Regular>
) : BaseBindingListAdapter<Regular>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_combo_regular;
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBindingViewHolder<Regular> {
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent, false
        ).also {
            Log.d("OrderProductAdapter", "RecycleView Height:" + parent.height);
            val height = parent.resources.getDimension(R.dimen._75sdp);
            val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                height.toInt()
            );
            (it as ItemComboRegularBinding).layoutMain.layoutParams = params;
            return BaseBindingViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<Regular>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        (holder.binding as ItemComboRegularBinding).root.setOnClickListener {
            listener.onItemClick(position,item);
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<Regular>() {
        override fun areItemsTheSame(oldItem: Regular, newItem: Regular): Boolean {
            return false;
        }

        override fun areContentsTheSame(
            oldItem: Regular,
            newItem: Regular
        ): Boolean {
            return false;
        }

    }
}