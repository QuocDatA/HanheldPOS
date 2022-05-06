package com.hanheldpos.ui.screens.buy_x_get_y.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.hanheldpos.R
import com.hanheldpos.binding.setPricePlusView
import com.hanheldpos.data.api.pojo.discount.CustomerBuys
import com.hanheldpos.databinding.ItemComboRegularBinding
import com.hanheldpos.model.cart.Regular
import com.hanheldpos.ui.base.adapter.BaseBindingListAdapter
import com.hanheldpos.ui.base.adapter.BaseBindingViewHolder

class BuyXGetYItemPickerAdapter(private val customerBuys: CustomerBuys? = null): BaseBindingListAdapter<Regular>(DiffCallBack()) {

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
//        productChosen.find { it.proOriginal?._id == item.proOriginal?._id }?.let {
//            (holder.binding as ItemComboRegularBinding).isChosen = true;
//        }
        holder.bindItem(item);
//        val binding = holder.binding as ItemComboRegularBinding;
//        val price = item.groupPrice(groupBundle,proOriginal);
//        if(price > 0){
//            setPricePlusView(binding.priceProduct,price)
//        }
//        else binding.priceProduct.visibility = View.GONE
//        (holder.binding as ItemComboRegularBinding).root.setOnClickListener {
//            listener.onItemClick(position,item);
//        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<Regular>() {
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