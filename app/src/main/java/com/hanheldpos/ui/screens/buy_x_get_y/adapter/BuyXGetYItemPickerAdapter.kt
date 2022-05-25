package com.hanheldpos.ui.screens.buy_x_get_y.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.binding.setPricePlusView
import com.hanheldpos.databinding.ItemComboRegularBinding
import com.hanheldpos.extension.setOnClickDebounce
import com.hanheldpos.model.cart.BaseProductInCart
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder
import com.hanheldpos.ui.base.adapter.BaseItemClickListener

class BuyXGetYItemPickerAdapter(
    private val listener: BaseItemClickListener<BaseProductInCart>
) : BaseBindingListAdapter<BaseProductInCart>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_combo_regular;
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBindingViewHolder<BaseProductInCart> {
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent, false
        ).also {
            val height = parent.resources.getDimension(R.dimen._75sdp);
            val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                height.toInt()
            );
            (it as ItemComboRegularBinding).layoutMain.layoutParams = params;
            return BaseBindingViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<BaseProductInCart>, position: Int) {
        val item = getItem(position);
        holder.bindItem(item);
        val binding = holder.binding as ItemComboRegularBinding;
        val price = item.proOriginal?.Price ?: 0.0
        if (price > 0) {
            setPricePlusView(binding.priceProduct, price)
        } else binding.priceProduct.visibility = View.GONE
        holder.binding.root.setOnClickListener {
            listener.onItemClick(position, item);
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<BaseProductInCart>() {
        override fun areItemsTheSame(oldItem: BaseProductInCart, newItem: BaseProductInCart): Boolean {
            return false;
        }

        override fun areContentsTheSame(
            oldItem: BaseProductInCart,
            newItem: BaseProductInCart
        ): Boolean {
            return false;
        }

    }
}